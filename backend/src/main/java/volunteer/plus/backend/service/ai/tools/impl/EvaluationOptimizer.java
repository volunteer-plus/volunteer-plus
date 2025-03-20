package volunteer.plus.backend.service.ai.tools.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Slf4j
@Service
public class EvaluationOptimizer implements AIAgentPattern {

    @Override
    public AIAgentPatternType getType() {
        return AIAgentPatternType.EVALUATION_OPTIMIZER;
    }

    public record Generation(String thoughts, String response) {
    }

    public record RefinedResponse(String solution, List<Generation> chainOfThought) {
    }

    public record EvaluationResponse(Evaluation evaluation, String feedback) {
        public enum Evaluation {
            PASS, NEEDS_IMPROVEMENT, FAIL
        }
    }

    @Tool(name = "patternEvaluate")
    public static RefinedResponse loop(final String task,
                                       final ChatClient chatClient,
                                       final String generatorPrompt,
                                       final String evaluatorPrompt) {
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
