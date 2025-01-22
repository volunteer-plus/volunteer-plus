package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.config.ai.FunctionalAIConfiguration;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.util.FunctionMethodNameCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;
import static volunteer.plus.backend.domain.enums.FileType.MP3;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatClient chatClient;
    private final OpenAiImageModel imageModel;
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;
    private final FunctionMethodNameCollector functionMethodNameCollector;

    @SneakyThrows
    public OpenAIServiceImpl(final OpenAiImageModel imageModel,
                             final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
                             final OpenAiAudioSpeechModel openAiAudioSpeechModel,
                             final FunctionMethodNameCollector functionMethodNameCollector,
                             final ChatClient.Builder builder,
                             final ChatMemory chatMemory,
                             final VectorStore vectorStore,
                             final @Value("classpath:/prompts/default_system_ai_prompt.txt") Resource defaultSystemPrompt,
                             final @Value("${ai.chat.history.window.size}") Integer chatWindowSize) {
        this.imageModel = imageModel;
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
        this.functionMethodNameCollector = functionMethodNameCollector;
        this.chatClient = builder
                .defaultSystem(defaultSystemPrompt)
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @Override
    public ChatResponse chat(final String message) {
        log.info("Asking GPT: {}", message);

        final Set<String> functionMethodNames = functionMethodNameCollector.getFunctionBeanMethodNames(FunctionalAIConfiguration.class);

        final Prompt prompt = new Prompt(
                message,
                OpenAiChatOptions
                        .builder()
                        .withFunctions(functionMethodNames)
                        .build()
        );

        return chatClient.prompt(prompt)
                .call()
                .chatResponse();
    }

    @Override
    public List<String> generateImage(final ImageGenerationRequestDTO imageGenerationRequestDTO) {
        log.info("Asking GPT to generate an image(s): {}", imageGenerationRequestDTO.getPrompt());

        final ImageResponse response = imageModel.call(
                new ImagePrompt(
                        imageGenerationRequestDTO.getPrompt(),
                        OpenAiImageOptions.builder()
                                .withQuality(imageGenerationRequestDTO.getQuality())
                                .withN(imageGenerationRequestDTO.getNumber())
                                .withHeight(imageGenerationRequestDTO.getHeight())
                                .withWidth(imageGenerationRequestDTO.getWidth())
                                .build()
                )
        );

        return response.getResults() == null ?
                new ArrayList<>() :
                response.getResults()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ImageGeneration::getOutput)
                        .filter(Objects::nonNull)
                        .map(Image::getUrl)
                        .filter(Objects::nonNull)
                        .toList();
    }

    @Override
    public String generateTextFromAudio(final String lang,
                                        final MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null || !file.getOriginalFilename().contains(MP3.getName())) {
            throw new ApiException(ErrorCode.WRONG_FILE_FORMAT);
        }

        log.info("Start analyzing speech file: {}", file.getOriginalFilename());

        final OpenAiAudioApi.TranscriptResponseFormat responseFormat = OpenAiAudioApi.TranscriptResponseFormat.TEXT;

        final OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage(lang)
                .withTemperature(0.8f)
                .withResponseFormat(responseFormat)
                .build();

        final AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(file.getResource(), transcriptionOptions);
        final AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(transcriptionRequest);

        log.info("Finished analyzing speech file: {}", file.getOriginalFilename());

        return response.getResult().getOutput();
    }

    @Override
    public ResponseEntity<byte[]> generateAudioFromText(final String message) {
        log.info("Start generating audio from speech..");

        final OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withSpeed(0.8f)
                .withModel(OpenAiAudioApi.TtsModel.TTS_1.value)
                .build();

        final SpeechPrompt speechPrompt = new SpeechPrompt(message, speechOptions);
        final SpeechResponse response = this.openAiAudioSpeechModel.call(speechPrompt);

        log.info("Finished generating audio from speech..");

        final byte[] responseAsBytes = response.getResult().getOutput();

        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=output.mp3")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseAsBytes);
    }
}
