package volunteer.plus.backend.service.ai.patterns.impl;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Bean;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.ai.patterns.AIAgentPattern;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Builder
public class ParallelizationWorkflow implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.PARALLELIZATION_WORKFLOW;
    }

    @Bean
    @Tool(name = "patternParallelization")
    public static List<String> parallel(final String prompt,
                                        final List<String> inputs,
                                        final int nWorkers,
                                        final ChatClient chatClient) {
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
