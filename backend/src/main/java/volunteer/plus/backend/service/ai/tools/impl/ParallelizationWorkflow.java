package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class ParallelizationWorkflow implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.PARALLELIZATION_WORKFLOW;
    }

    @Tool(name = "patternParallelization", description = """
        This tool executes a parallelized workflow to process a list of inputs using a provided prompt.
        The tool creates a fixed thread pool based on nWorkers, and for each input, it asynchronously calls the chatClient with the combined prompt.
        It waits for all tasks to complete and then aggregates the responses into a List of Strings, which is returned as the final result.
        In case of any exception during processing, an ApiException is thrown.
        """)
    public static List<String> parallel(@ToolParam(description = "A String that serves as the base prompt for each parallel execution.") final String prompt,
                                        @ToolParam(description = "A List of Strings, where each string is appended to the base prompt to create a unique input.") final List<String> inputs,
                                        @ToolParam(description = "An integer specifying the number of worker threads to use for parallel processing.") final int nWorkers,
                                        @ToolParam(description = "An instance of ChatClient to interact with the AI chat engine for processing each input.") final ChatClient chatClient) {
        try (final ExecutorService executor = Executors.newFixedThreadPool(nWorkers)) {
            final List<CompletableFuture<String>> futures = inputs.stream()
                    .map(input -> CompletableFuture.supplyAsync(() -> chatClient.prompt(prompt + "\nInput: " + input).call().content(), executor))
                    .toList();

            // Wait for all tasks to complete
            final CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
            allFutures.join();

            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
}
