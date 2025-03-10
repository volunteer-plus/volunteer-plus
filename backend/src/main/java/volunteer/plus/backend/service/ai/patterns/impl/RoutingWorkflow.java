package volunteer.plus.backend.service.ai.patterns.impl;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.ai.patterns.AIAgentPattern;

import java.util.Map;

@Slf4j
@Builder
public class RoutingWorkflow implements AIAgentPattern {
    private final ChatClient chatClient;

    public RoutingWorkflow(final ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.ROUTING_WORKFLOW;
    }

    public String route(final String input,
                        final Map<String, String> routes) {
        // Determine the appropriate route for the input
        final String routeKey = determineRoute(input, routes.keySet());

        // Get the selected prompt from the routes map
        final String selectedPrompt = routes.get(routeKey);

        if (selectedPrompt == null) {
            throw new ApiException("Selected route '" + routeKey + "' not found in routes map");
        }

        // Process the input with the selected prompt
        return chatClient.prompt(selectedPrompt + "\nInput: " + input).call().content();
    }

    private String determineRoute(final String input,
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

        if (routingResponse == null) {
            throw new ApiException("Undefined Routing Response");
        }

        log.info("Routing Analysis:{}\nSelected route: {}", routingResponse.reasoning(), routingResponse.selection());

        return routingResponse.selection();
    }

    public record RoutingResponse(String reasoning, String selection) {
    }
}
