package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
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

    @Tool(name = "patternChain", description = """
        This tool implements a prompt chaining workflow.
        The tool processes the user input through each prompt in the promptList in sequence.
        For each prompt, it creates an input by combining the current prompt with the current response.
        It then sends this combined input to the chatClient and updates the response with the result.
        After processing all prompts, the final output is returned as the result of the chaining process.
        """)
    public static String chain(@ToolParam(description = "A String representing the initial input provided by the user.") final String userInput,
                               @ToolParam(description = "An instance of ChatClient used to interact with the AI chat engine.") final ChatClient chatClient,
                               @ToolParam(description = " A List of Strings, each representing a prompt that will be sequentially applied.") final List<String> promptList) {
        String response = userInput;

        for (String prompt : promptList) {
            final String input = "Prompt:\n" + prompt + "\n User Input:\n" + response;
            response = chatClient.prompt(input).call().content();
        }

        return response;
    }
}
