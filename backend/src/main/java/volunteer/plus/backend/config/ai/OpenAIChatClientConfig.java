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
public class OpenAIChatClientConfig {
    private final Integer chatWindowSize;
    private final Resource defaultSystemPrompt;
    private final VectorStore openAiVectorStore;
    private final ChatMemory chatMemory;

    public OpenAIChatClientConfig(final @Autowired(required = false) @Qualifier("openAiVectorStore") VectorStore openAiVectorStore,
                                  final ChatMemory chatMemory,
                                  final @Value("${ai.chat.history.window.size}") Integer chatWindowSize,
                                  final @Value("classpath:/prompts/default_system_ai_prompt.txt") Resource defaultSystemPrompt) {
        this.openAiVectorStore = openAiVectorStore;
        this.chatMemory = chatMemory;
        this.chatWindowSize = chatWindowSize;
        this.defaultSystemPrompt = defaultSystemPrompt;
    }

    @Bean
    public ChatClient generalChatClient(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                        final @Qualifier("openAiChatModel") ChatModel chatModel) {
        final ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }


    @Bean
    public ChatClient militaryChatClient(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                         final @Qualifier("openAiChatModel") ChatModel chatModel) {
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
    public ChatClient inMemoryChatClient(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                         final @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel,
                                         final @Qualifier("openAiChatModel") ChatModel chatModel) {
        final ChatClient.Builder builder = ChatClient.builder(chatModel);

        final VectorStore vectorStore = openAiVectorStore != null ?
                openAiVectorStore :
                SimpleVectorStore.builder(embeddingModel).build();

        return chatClientBuilderConfigurer.configure(builder)
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean("openAIChatClientMap")
    public Map<AIChatClient, ChatClient> openAIChatClientMap(final ChatClient generalChatClient,
                                                             final ChatClient militaryChatClient,
                                                             final ChatClient inMemoryChatClient) {
        return Map.of(
                AIChatClient.OPENAI_DEFAULT, generalChatClient,
                AIChatClient.OPENAI_MILITARY, militaryChatClient,
                AIChatClient.OPENAI_IN_MEMORY, inMemoryChatClient
        );
    }
}
