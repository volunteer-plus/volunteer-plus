package volunteer.plus.backend.service.ai.patterns.impl;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import volunteer.plus.backend.service.ai.patterns.AIAgentPattern;

import java.util.List;

@Slf4j
@Builder
public class PromptChainWorkflow implements AIAgentPattern {
    private final ChatClient chatClient;

    private final List<String> promptList;

    public String chain(String userInput) {
        String response = userInput;

        for (String prompt : promptList) {
            final String input = "Prompt:\n" + prompt + "\n User Input:\n" + response;
            response = chatClient.prompt(input).call().content();
        }

        return response;
    }
}
