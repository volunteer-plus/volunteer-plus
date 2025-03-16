package volunteer.plus.backend.service.ai.patterns.impl;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Bean;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.patterns.AIAgentPattern;

import java.util.List;

@Slf4j
@Builder
public class PromptChainWorkflow implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.PROMPT_CHAIN_WORKFLOW;
    }

    @Bean
    @Tool(name = "patternChain")
    public static String chain(final String userInput,
                               final ChatClient chatClient,
                               final List<String> promptList) {
        String response = userInput;

        for (String prompt : promptList) {
            final String input = "Prompt:\n" + prompt + "\n User Input:\n" + response;
            response = chatClient.prompt(input).call().content();
        }

        return response;
    }
}
