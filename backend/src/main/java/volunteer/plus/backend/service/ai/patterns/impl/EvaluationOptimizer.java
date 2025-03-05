package volunteer.plus.backend.service.ai.patterns.impl;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.ai.patterns.AIAgentPattern;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
public class EvaluationOptimizer implements AIAgentPattern {
    private final ChatClient chatClient;

    private final String generatorPrompt;

    private final String evaluatorPrompt;

    public EvaluationOptimizer(final ChatClient chatClient,
                               final String generatorPrompt,
                               final String evaluatorPrompt) {
        this.chatClient = chatClient;
        this.generatorPrompt = generatorPrompt;
        this.evaluatorPrompt = evaluatorPrompt;
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


    public RefinedResponse loop(String task) {
        final List<String> memory = new ArrayList<>();
        final List<Generation> chainOfThought = new ArrayList<>();

        return loop(task, "", memory, chainOfThought);
    }

    private RefinedResponse loop(final String task,
                                 final String context,
                                 final List<String> memory,
                                 final List<Generation> chainOfThought) {

        final Generation generation = generate(task, context);
        memory.add(generation.response());
        chainOfThought.add(generation);

        final EvaluationResponse evaluationResponse = evaluate(generation.response(), task);

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

        return loop(task, newContext.toString(), memory, chainOfThought);
    }

    private Generation generate(final String task,
                                final String context) {
        final Generation generationResponse = chatClient.prompt()
                .user(u -> u.text("{prompt}\n{context}\nTask: {task}")
                        .param("prompt", this.generatorPrompt)
                        .param("context", context)
                        .param("task", task))
                .call()
                .entity(Generation.class);

        if (generationResponse == null) {
            throw new ApiException("Undefined Generation Response");
        }

        log.info("\n=== GENERATOR OUTPUT ===\nTHOUGHTS: {}\n\nRESPONSE:\n {}\n", generationResponse.thoughts(), generationResponse.response());

        return generationResponse;
    }

    private EvaluationResponse evaluate(final String content,
                                        final String task) {
        final EvaluationResponse evaluationResponse = chatClient.prompt()
                .user(u -> u.text("{prompt}\nOriginal task: {task}\nContent to evaluate: {content}")
                        .param("prompt", this.evaluatorPrompt)
                        .param("task", task)
                        .param("content", content))
                .call()
                .entity(EvaluationResponse.class);

        if (evaluationResponse == null) {
            throw new ApiException("Undefined Evaluation Response");
        }

        log.info("\n=== EVALUATOR OUTPUT ===\nEVALUATION: {}\n\nFEEDBACK: {}\n", evaluationResponse.evaluation(), evaluationResponse.feedback());

        return evaluationResponse;
    }
}
