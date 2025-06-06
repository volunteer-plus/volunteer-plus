package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.config.ai.evaluators.EnhancedRelevancyEvaluator;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.service.ai.AIModerationService;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.service.websocket.WebSocketService;
import volunteer.plus.backend.util.AIClientProviderUtil;
import volunteer.plus.backend.domain.dto.ByteArrayMultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.*;
import static volunteer.plus.backend.domain.enums.FileType.MP3;
import static volunteer.plus.backend.util.AIUtil.getAIMediaList;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private static final String RESPONSE = "\nResponse:\n";
    private static final String OPEN_AI_REQUEST_IMAGE_GENERATION = "OpenAI request image generation:\n";

    private final AIClientProviderUtil aiClientProviderUtil;
    private final OpenAiImageModel imageModel;
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;
    private final AIModerationService moderationService;
    private final ChatModel chatModel;
    private final OpenAIService openAIService;
    private final List<ToolCallback> tools;
    private final WebSocketService webSocketService;

    @SneakyThrows
    public OpenAIServiceImpl(final AIClientProviderUtil aiClientProviderUtil,
                             final OpenAiImageModel imageModel,
                             final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
                             final OpenAiAudioSpeechModel openAiAudioSpeechModel,
                             final AIModerationService moderationService,
                             final @Qualifier("openAiChatModel") ChatModel chatModel,
                             final @Lazy OpenAIService openAIService,
                             final List<ToolCallback> tools,
                             final WebSocketService webSocketService) {
        this.aiClientProviderUtil = aiClientProviderUtil;
        this.imageModel = imageModel;
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
        this.moderationService = moderationService;
        this.chatModel = chatModel;
        this.openAIService = openAIService;
        this.tools = tools;
        this.webSocketService = webSocketService;
    }

    @Override
    @SneakyThrows
    public AIChatResponse chat(final AIChatClient chatClient,
                               final String message,
                               final List<MultipartFile> multipartFiles) {
        log.info("Asking GPT: {}", message);

        // call async process of message moderation
        final Future<ModerationResponse> moderationFuture = moderationService.moderate(message);

        final UserMessage um = new UserMessage(
                message,
                getAIMediaList(multipartFiles)
        );

        final ChatClient client = aiClientProviderUtil.getChatClient(chatClient);

        if (client == null) {
            throw new ApiException(ErrorCode.CHAT_CLIENT_NOT_FOUND);
        }

        final FactCheckingEvaluator factCheckingEvaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));
        final EnhancedRelevancyEvaluator relevancyEvaluator = new EnhancedRelevancyEvaluator(ChatClient.builder(chatModel));

        final String response = client.prompt(new Prompt(um))
                .tools(tools)
                .call()
                .content();

        final EvaluationRequest evaluationRequest = new EvaluationRequest(message, response);

        webSocketService.sendNotification(OPENAI_CHAT_CLIENT_TARGET, "OpenAI request:\n" + message + RESPONSE + response);

        return AIChatResponse.builder()
                .chatResponse(response)
                .moderationResponse(moderationFuture.get())
                .relevancyResponse(relevancyEvaluator.evaluate(evaluationRequest))
                .factCheckingResponse(factCheckingEvaluator.evaluate(evaluationRequest))
                .build();
    }

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> generateImage(final ImageGenerationRequestDTO imageGenerationRequestDTO) {
        final Image image = getImage(imageGenerationRequestDTO);

        webSocketService.sendNotification(OPENAI_IMAGE_CLIENT_TARGET, OPEN_AI_REQUEST_IMAGE_GENERATION + imageGenerationRequestDTO.getPrompt() + RESPONSE + image.getUrl());

        try {
            final URL specUrl = URI.create(image.getUrl()).toURL();
            try (final InputStream is = specUrl.openStream()) {
                return ResponseEntity.ok()
                        .header("content-disposition", "attachment; filename=output.png")
                        .body(is.readAllBytes());
            }
        } catch (Exception e) {
            throw new ApiException(ErrorCode.CANNOT_DOWNLOAD_OUTPUT_FILE);
        }
    }

    @SneakyThrows
    @Override
    public MultipartFile generateImageAsMultipartFile(final ImageGenerationRequestDTO imageGenerationRequestDTO) {
        final Image image = getImage(imageGenerationRequestDTO);

        webSocketService.sendNotification(OPENAI_IMAGE_CLIENT_TARGET,
                OPEN_AI_REQUEST_IMAGE_GENERATION +
                        imageGenerationRequestDTO.getPrompt() +
                        RESPONSE +
                        image.getUrl()
        );

        try {
            final URL specUrl = URI.create(image.getUrl()).toURL();
            try (final InputStream is = specUrl.openStream()) {
                byte[] fileBytes = is.readAllBytes();
                return new ByteArrayMultipartFile("file", "output.png", "image/png", fileBytes);
            }
        } catch (Exception e) {
            throw new ApiException(ErrorCode.CANNOT_DOWNLOAD_OUTPUT_FILE);
        }
    }

    private Image getImage(final ImageGenerationRequestDTO imageGenerationRequestDTO) throws InterruptedException, ExecutionException {
        log.info("Asking GPT to generate an image: {}", imageGenerationRequestDTO.getPrompt());

        final ImageResponse response = openAIService.getImageResponse(imageGenerationRequestDTO, 1).get();
        final Image image = response.getResult().getOutput();

        if (image == null || image.getUrl() == null) {
            throw new ApiException(ErrorCode.EMPTY_FILE);
        }

        return image;
    }


    @SneakyThrows
    @Override
    public List<String> generateImageUrls(final ImageGenerationRequestDTO imageGenerationRequestDTO) {
        log.info("Asking GPT to generate an image(s) urls: {}", imageGenerationRequestDTO.getPrompt());

        final ImageResponse response = openAIService.getImageResponse(imageGenerationRequestDTO, imageGenerationRequestDTO.getNumber()).get();

        final List<String> urls = response.getResults() == null ?
                new ArrayList<>() :
                response.getResults()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ImageGeneration::getOutput)
                        .filter(Objects::nonNull)
                        .map(Image::getUrl)
                        .filter(Objects::nonNull)
                        .toList();

        urls.forEach(url -> webSocketService.sendNotification(OPENAI_IMAGE_CLIENT_TARGET, OPEN_AI_REQUEST_IMAGE_GENERATION + imageGenerationRequestDTO.getPrompt() + RESPONSE + url));

        return urls;
    }

    @Async("asyncExecutor")
    public Future<ImageResponse> getImageResponse(final ImageGenerationRequestDTO imageGenerationRequestDTO,
                                                  final int quantity) {
        return CompletableFuture.completedFuture(
                imageModel.call(
                        new ImagePrompt(
                                imageGenerationRequestDTO.getPrompt(),
                                OpenAiImageOptions.builder()
                                        .withQuality(imageGenerationRequestDTO.getQuality())
                                        .withN(quantity)
                                        .withHeight(imageGenerationRequestDTO.getHeight())
                                        .withWidth(imageGenerationRequestDTO.getWidth())
                                        .build()
                        )
                )
        );
    }

    @Override
    public String generateTextFromAudio(final String lang,
                                        final MultipartFile file) {
        if (file == null || !file.getOriginalFilename().contains(MP3.getName())) {
            throw new ApiException(ErrorCode.WRONG_FILE_FORMAT);
        }

        log.info("Start analyzing speech file: {}", file.getOriginalFilename());

        final OpenAiAudioApi.TranscriptResponseFormat responseFormat = OpenAiAudioApi.TranscriptResponseFormat.TEXT;

        final OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .language(lang)
                .temperature(0.8f)
                .responseFormat(responseFormat)
                .build();

        final AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(file.getResource(), transcriptionOptions);
        final AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(transcriptionRequest);

        log.info("Finished analyzing speech file: {}", file.getOriginalFilename());

        webSocketService.sendNotification(OPENAI_SPEECH_TO_TEXT_CLIENT_TARGET, "OpenAI request text from speech generation:\n" + response.getResult().getOutput());

        return response.getResult().getOutput();
    }

    @Override
    public ResponseEntity<byte[]> generateAudioFromText(final String message) {
        log.info("Start generating audio from speech..");

        final OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(0.8f)
                .model(OpenAiAudioApi.TtsModel.TTS_1.value)
                .build();

        final SpeechPrompt speechPrompt = new SpeechPrompt(message, speechOptions);
        final SpeechResponse response = this.openAiAudioSpeechModel.call(speechPrompt);

        log.info("Finished generating audio from speech..");

        final byte[] responseAsBytes = response.getResult().getOutput();

        webSocketService.sendNotification(OPENAI_TEXT_TO_SPEECH_CLIENT_TARGET, "OpenAI request speech from text generation:\n" + "Request:\n" + message);

        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=output.mp3")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseAsBytes);
    }
}
