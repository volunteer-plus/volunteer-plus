package volunteer.plus.backend.config.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import volunteer.plus.backend.config.ai.advisor.Re2Advisor;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;

@Configuration
@RequiredArgsConstructor
public class ChatClientBuildingConfig {
    @Value("${ai.chat.history.window.size}")
    private Integer chatWindowSize;

    @Value("classpath:/prompts/default_system_ai_prompt.txt")
    private Resource defaultSystemPrompt;

    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    @Bean
    public ChatClient generalChatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new Re2Advisor()
                )
                .build();
    }


    @Bean
    public ChatClient militaryChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(defaultSystemPrompt)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }

    @Bean
    public ChatClient inMemoryChatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, chatWindowSize),
                        new SimpleLoggerAdvisor(),
                        new Re2Advisor()
                )
                .build();
    }
}
