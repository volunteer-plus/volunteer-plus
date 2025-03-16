package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.List;

@Slf4j
@Service
public class OrchestratorWorkers implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.ORCHESTRATOR_WORKERS;
    }

    public record Task(String type, String description) {
    }

    public record OrchestratorResponse(String analysis, List<Task> tasks) {
    }

    public record FinalResponse(String analysis, List<String> workerResponses) {
    }

    @Tool(name = "patternOrchestrator")
    public static FinalResponse process(final String taskDescription,
                                        final ChatClient chatClient,
                                        final String orchestratorPrompt,
                                        final String workerPrompt) {
        final OrchestratorResponse orchestratorResponse = chatClient.prompt()
                .user(u -> u.text(orchestratorPrompt).param("task", taskDescription))
                .call()
                .entity(OrchestratorResponse.class);

        log.info("\n=== ORCHESTRATOR OUTPUT ===\nANALYSIS: {}\n\nTASKS: {}\n", orchestratorResponse.analysis(), orchestratorResponse.tasks());

        final List<String> workerResponses = orchestratorResponse.tasks()
                .stream()
                .map(task -> chatClient.prompt()
                .user(u -> u.text(workerPrompt)
                        .param("original_task", taskDescription)
                        .param("task_type", task.type())
                        .param("task_description", task.description()))
                .call()
                .content()).toList();

        System.out.println("\n=== WORKER OUTPUT ===\n" + workerResponses);

        return new FinalResponse(orchestratorResponse.analysis(), workerResponses);
    }
}
