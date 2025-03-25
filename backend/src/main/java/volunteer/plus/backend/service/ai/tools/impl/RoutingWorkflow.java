package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.Map;

@SuppressWarnings("unused")
@Slf4j
@Service
public class RoutingWorkflow implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.ROUTING_WORKFLOW;
    }

    @Tool(name = "patternRoute", description = """
        This tool implements a routing workflow for processing an input based on dynamically selected routes.
        The tool works as follows:
          1. It determines the most appropriate route by sending the input along with available route keys to the chatClient.
          2. The chatClient returns a JSON response with reasoning and a selection, indicating the best matching route.
          3. The tool retrieves the prompt associated with the selected route from the routes map.
          4. The input is then processed using the selected prompt, and the resulting output is returned.
        If the selected route is not found in the routes map, an ApiException is thrown.
        """)
    public static String route(@ToolParam(description = "input: A String representing the user's input that needs to be routed.") final String input,
                               @ToolParam(description = "A Map<String, String> where keys represent possible route identifiers and values are the corresponding prompts.") final Map<String, String> routes,
                               @ToolParam(description = "An instance of ChatClient used to interact with the AI chat engine.") final ChatClient chatClient) {
        // Determine the appropriate route for the input
        final String routeKey = determineRoute(input, chatClient, routes.keySet());

        // Get the selected prompt from the routes map
        final String selectedPrompt = routes.get(routeKey);

        if (selectedPrompt == null) {
            throw new ApiException("Selected route '" + routeKey + "' not found in routes map");
        }

        // Process the input with the selected prompt
        return chatClient.prompt(selectedPrompt + "\nInput: " + input).call().content();
    }

    private static String determineRoute(final String input,
                                         final ChatClient chatClient,
                                         final Iterable<String> availableRoutes) {
        System.out.println("\nAvailable routes: " + availableRoutes);

        String selectorPrompt = String.format("""
                Analyze the input and select the most appropriate support team from these options: %s
                First explain your reasoning, then provide your selection in this JSON format:

                \\{
                    "reasoning": "Brief explanation of why",
                    "selection": "The resulted selection"
                \\}

                Input: %s""", availableRoutes, input);

        final RoutingResponse routingResponse = chatClient.prompt(selectorPrompt).call().entity(RoutingResponse.class);

        log.info("Routing Analysis:{}\nSelected route: {}", routingResponse.reasoning(), routingResponse.selection());

        return routingResponse.selection();
    }

    public record RoutingResponse(String reasoning, String selection) {
    }
}
