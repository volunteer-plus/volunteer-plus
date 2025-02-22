package volunteer.plus.backend.config.ai;

import org.springframework.ai.autoconfigure.chat.client.ChatClientBuilderConfigurer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import volunteer.plus.backend.config.ai.advisor.Re2Advisor;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;

@Configuration
public class ChatClientBuildingConfig {
    private final Integer chatWindowSize;
    private final Resource defaultSystemPrompt;
    private final VectorStore openAiVectorStore;
    private final ChatMemory chatMemory;

    public ChatClientBuildingConfig(final @Qualifier("openAiVectorStore") VectorStore openAiVectorStore,
                                    final ChatMemory chatMemory,
                                    final @Value("${ai.chat.history.window.size}") Integer chatWindowSize,
                                    final @Value("classpath:/prompts/default_system_ai_prompt.txt") Resource defaultSystemPrompt) {
        this.openAiVectorStore = openAiVectorStore;
        this.chatMemory = chatMemory;
        this.chatWindowSize = chatWindowSize;
        this.defaultSystemPrompt = defaultSystemPrompt;
    }

    @Bean
    public ChatClient.Builder openAiChatClientBuilder(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                                      final @Qualifier("openAiChatModel") ChatModel chatModel) {
        return getChatClientBuilder(chatClientBuilderConfigurer, chatModel);
    }

    @Bean
    public ChatClient.Builder ollamaChatClientBuilder(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                                      final @Qualifier("ollamaChatModel") ChatModel chatModel) {
        return getChatClientBuilder(chatClientBuilderConfigurer, chatModel);
    }

    @Bean
    public ChatClient ollamaGeneralChatClient(ChatClient.Builder ollamaChatClientBuilder) {
        return ollamaChatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean
    public ChatClient generalChatClient(ChatClient.Builder openAiChatClientBuilder) {
        return openAiChatClientBuilder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new Re2Advisor()
                )
                .build();
    }


    @Bean
    public ChatClient militaryChatClient(ChatClient.Builder openAiChatClientBuilder) {
        return openAiChatClientBuilder
                .defaultSystem(defaultSystemPrompt)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean
    public ChatClient inMemoryChatClient(ChatClient.Builder openAiChatClientBuilder) {
        return openAiChatClientBuilder
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(openAiVectorStore),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    private ChatClient.Builder getChatClientBuilder(final ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                                    final ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder);
    }
}
