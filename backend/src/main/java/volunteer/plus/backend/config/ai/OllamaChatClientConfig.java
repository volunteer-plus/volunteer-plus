package volunteer.plus.backend.config.ai;

import org.springframework.ai.autoconfigure.chat.client.ChatClientBuilderConfigurer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import volunteer.plus.backend.config.ai.advisor.Re2Advisor;
import volunteer.plus.backend.domain.enums.AIChatClient;

import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;

@Configuration
public class OllamaChatClientConfig {
    private final Integer chatWindowSize;
    private final Resource defaultSystemPrompt;
    private final VectorStore ollamaVectorStore;
    private final ChatMemory chatMemory;

    public OllamaChatClientConfig(final @Autowired(required = false) @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore,
                                  final ChatMemory chatMemory,
                                  final @Value("${ai.chat.history.window.size}") Integer chatWindowSize,
                                  final @Value("classpath:/prompts/default_system_ai_prompt.txt") Resource defaultSystemPrompt) {
        this.ollamaVectorStore = ollamaVectorStore;
        this.chatMemory = chatMemory;
        this.chatWindowSize = chatWindowSize;
        this.defaultSystemPrompt = defaultSystemPrompt;
    }

    @Bean
    public ChatClient ollamaGeneralChatClient(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                              final @Qualifier("ollamaChatModel") ChatModel chatModel) {
        final ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean
    public ChatClient ollamaMilitaryChatClient(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                               final @Qualifier("ollamaChatModel") ChatModel chatModel) {
        final ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder)
                .defaultSystem(defaultSystemPrompt)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean
    public ChatClient ollamaInMemoryChatClient(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                               final @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel,
                                               final @Qualifier("ollamaChatModel") ChatModel chatModel) {
        final VectorStore vectorStore = ollamaVectorStore != null ? ollamaVectorStore : SimpleVectorStore.builder(embeddingModel).build();
        final ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder)
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean("ollamaChatClientMap")
    public Map<AIChatClient, ChatClient> ollamaChatClientMap(final ChatClient ollamaGeneralChatClient,
                                                             final ChatClient ollamaMilitaryChatClient,
                                                             final ChatClient ollamaInMemoryChatClient) {
        return Map.of(
                AIChatClient.DEFAULT, ollamaGeneralChatClient,
                AIChatClient.MILITARY, ollamaMilitaryChatClient,
                AIChatClient.IN_MEMORY, ollamaInMemoryChatClient
        );
    }
}
