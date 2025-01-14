package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.config.ai.FunctionalAIConfiguration;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.util.FunctionMethodNameCollector;

import java.util.Set;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatClient chatClient;
    private final FunctionMethodNameCollector functionMethodNameCollector;

    @SneakyThrows
    public OpenAIServiceImpl(final FunctionMethodNameCollector functionMethodNameCollector,
                             final ChatClient.Builder builder,
                             final VectorStore vectorStore,
                             final @Value("classpath:/prompts/default_system_ai_prompt.txt") Resource defaultSystemPrompt) {
        this.functionMethodNameCollector = functionMethodNameCollector;
        this.chatClient = builder
                .defaultSystem(defaultSystemPrompt)
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
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
}
