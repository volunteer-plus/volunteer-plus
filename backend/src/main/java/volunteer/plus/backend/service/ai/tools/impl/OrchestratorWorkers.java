package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.ai.agent.FinalResponse;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.List;

@SuppressWarnings("unused")
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

    @Tool(name = "patternOrchestrator", description = """
        This tool coordinates the orchestration of worker tasks based on an initial task description.
        The tool first sends the taskDescription to the orchestrator prompt, which returns an analysis and a list of tasks.
        Then, each task is sent to the worker prompt to generate a corresponding worker response.
        Finally, the tool aggregates the orchestrator's analysis and the worker responses into a FinalResponse.
        """)
    public static FinalResponse process(@ToolParam(description = "A String describing the original task or problem.") final String taskDescription,
                                        @ToolParam(description = "An instance of ChatClient for interacting with the AI chat engine.") final ChatClient chatClient,
                                        @ToolParam(description = "A String prompt that instructs the orchestrator to analyze the original task and generate a list of worker tasks, each defined by a type and description.") final String orchestratorPrompt,
                                        @ToolParam(description = "A String prompt that instructs individual workers to execute their assigned tasks, using the original task, task type, and task description.") final String workerPrompt) {
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
