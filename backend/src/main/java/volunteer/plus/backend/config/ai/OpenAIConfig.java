package volunteer.plus.backend.config.ai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
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
