package volunteer.plus.backend.config.ai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import volunteer.plus.backend.repository.AIChatMessageRepository;
import volunteer.plus.backend.service.ai.impl.AIMemoryServiceImpl;

@Configuration
public class OpenAIConfig {

    private final AIChatMessageRepository aiChatMessageRepository;

    public OpenAIConfig(final AIChatMessageRepository aiChatMessageRepository) {
        this.aiChatMessageRepository = aiChatMessageRepository;
    }

    @Bean
    public ChatMemory chatMemory() {
        return new AIMemoryServiceImpl(aiChatMessageRepository);
    }

    @Bean
    public OpenAiImageModel imageClient(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return new OpenAiImageModel(new OpenAiImageApi(apiKey));
    }

    @Bean
    public OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return new OpenAiAudioTranscriptionModel(new OpenAiAudioApi(apiKey));
    }

    @Bean
    public OpenAiAudioSpeechModel openAiAudioSpeechModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return new OpenAiAudioSpeechModel(new OpenAiAudioApi(apiKey));
    }

    @Bean
    public TextSplitter textSplitter() {
        return new TokenTextSplitter();
    }
}
