package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.List;

@SuppressWarnings("unused")
@Slf4j
@Service
public class PromptChainWorkflow implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.PROMPT_CHAIN_WORKFLOW;
    }

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
