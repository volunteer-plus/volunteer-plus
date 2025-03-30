package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.ai.agent.EvaluationResponse;
import volunteer.plus.backend.domain.dto.ai.agent.Generation;
import volunteer.plus.backend.domain.dto.ai.agent.RefinedResponse;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EvaluationOptimizer implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.EVALUATION_OPTIMIZER;
    }

    @Tool(name = "patternEvaluate", description = """
        This tool iteratively generates candidate responses for a given task using a chain-of-thought approach.
        The tool uses the generator prompt to produce potential answers and the evaluator prompt to assess them.
        It then iterates—accumulating previous responses and feedback—until a response passes the evaluation criteria.
        Finally, it returns the accepted solution along with the entire chain-of-thought for review.
        """)
    public static RefinedResponse loop(@ToolParam(description = "A String that describes the task or problem to be solved.") final String task,
                                       @ToolParam(description = "An instance of ChatClient used to interact with the AI chat engine.") final ChatClient chatClient,
                                       @ToolParam(description = "A String prompt that instructs the generator on how to produce candidate responses.") final String generatorPrompt,
                                       @ToolParam(description = "A String prompt that instructs the evaluator on how to assess the generated responses.") final String evaluatorPrompt) {
        final List<String> memory = new ArrayList<>();
        final List<Generation> chainOfThought = new ArrayList<>();

        return loop(task, "", memory, chainOfThought, chatClient, generatorPrompt, evaluatorPrompt);
    }

    private static RefinedResponse loop(final String task,
                                        final String context,
                                        final List<String> memory,
                                        final List<Generation> chainOfThought,
                                        final ChatClient chatClient,
                                        final String generatorPrompt,
                                        final String evaluatorPrompt) {

        final Generation generation = generate(task, context, chatClient, generatorPrompt);
        memory.add(generation.response());
        chainOfThought.add(generation);

        final EvaluationResponse evaluationResponse = evaluate(generation.response(), task, chatClient, evaluatorPrompt);

        if (evaluationResponse.evaluation().equals(EvaluationResponse.Evaluation.PASS)) {
            // Solution is accepted!
            return new RefinedResponse(generation.response(), chainOfThought);
        }

        final StringBuilder newContext = new StringBuilder();
        newContext.append("Previous attempts:");
        for (String m : memory) {
            newContext.append("\n- ").append(m);
        }
        newContext.append("\nFeedback: ").append(evaluationResponse.feedback());

        return loop(task, newContext.toString(), memory, chainOfThought, chatClient, generatorPrompt, evaluatorPrompt);
    }

    private static Generation generate(final String task,
                                       final String context,
                                       final ChatClient chatClient,
                                       final String generatorPrompt) {
        final Generation generationResponse = chatClient.prompt()
                .user(u -> u.text("{prompt}\n{context}\nTask: {task}")
                        .param("prompt", generatorPrompt)
                        .param("context", context)
                        .param("task", task))
                .call()
                .entity(Generation.class);

        log.info("\n=== GENERATOR OUTPUT ===\nTHOUGHTS: {}\n\nRESPONSE:\n {}\n", generationResponse.thoughts(), generationResponse.response());

        return generationResponse;
    }

    private static EvaluationResponse evaluate(final String content,
                                               final String task,
                                               final ChatClient chatClient,
                                               final String evaluatorPrompt) {
        final EvaluationResponse evaluationResponse = chatClient.prompt()
                .user(u -> u.text("{prompt}\nOriginal task: {task}\nContent to evaluate: {content}")
                        .param("prompt", evaluatorPrompt)
                        .param("task", task)
                        .param("content", content))
                .call()
                .entity(EvaluationResponse.class);

        log.info("\n=== EVALUATOR OUTPUT ===\nEVALUATION: {}\n\nFEEDBACK: {}\n", evaluationResponse.evaluation(), evaluationResponse.feedback());

        return evaluationResponse;
    }
}
