package volunteer.plus.backend.service.ai.patterns.impl;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.ai.patterns.AIAgentPattern;

import java.util.List;

@Slf4j
@Builder
public class OrchestratorWorkers implements AIAgentPattern {

    private final ChatClient chatClient;
    private final String orchestratorPrompt;
    private final String workerPrompt;

    public record Task(String type, String description) {
    }

    public record OrchestratorResponse(String analysis, List<Task> tasks) {
    }

    public record FinalResponse(String analysis, List<String> workerResponses) {
    }

    public OrchestratorWorkers(final ChatClient chatClient,
                               final String orchestratorPrompt,
                               final String workerPrompt) {
        this.chatClient = chatClient;
        this.orchestratorPrompt = orchestratorPrompt;
        this.workerPrompt = workerPrompt;
    }

    public FinalResponse process(final String taskDescription) {
        final OrchestratorResponse orchestratorResponse = this.chatClient.prompt()
                .user(u -> u.text(this.orchestratorPrompt).param("task", taskDescription))
                .call()
                .entity(OrchestratorResponse.class);

        if (orchestratorResponse == null) {
            throw new ApiException("Undefined Orchestrator Response");
        }

        log.info("\n=== ORCHESTRATOR OUTPUT ===\nANALYSIS: {}\n\nTASKS: {}\n", orchestratorResponse.analysis(), orchestratorResponse.tasks());

        final List<String> workerResponses = orchestratorResponse.tasks()
                .stream()
                .map(task -> this.chatClient.prompt()
                .user(u -> u.text(this.workerPrompt)
                        .param("original_task", taskDescription)
                        .param("task_type", task.type())
                        .param("task_description", task.description()))
                .call()
                .content()).toList();

        System.out.println("\n=== WORKER OUTPUT ===\n" + workerResponses);

        return new FinalResponse(orchestratorResponse.analysis(), workerResponses);
    }
}
