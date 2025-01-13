package volunteer.plus.backend.service.ai.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.ai.OpenAIService;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final ChatClient chatClient;

    public OpenAIServiceImpl(final ChatClient.Builder builder,
                             final VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @Override
    public String chat(final String message) {
        log.info("Asking GPT: {}", message);
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
