package volunteer.plus.backend.config.ai.evaluators;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.Evaluator;

import java.util.Collections;


public class EnhancedRelevancyEvaluator implements Evaluator {

    private static final String DEFAULT_EVALUATION_PROMPT_TEXT = """
				Your main task is to evaluate if the response for the query
				is in line with the context information provided.\\n
				You have two options to answer. Either YES/ NO.\\n
				Answer - YES, if the response for the query
				is in line with context information otherwise NO.\\n
				Query: \\n {query}\\n
				Response: \\n {response}\\n
				Context: \\n {context}\\n
				Answer: "
			""";

    private final ChatClient.Builder chatClientBuilder;
    private final String evaluationPrompt;

    public EnhancedRelevancyEvaluator(final ChatClient.Builder chatClientBuilder) {
        this(chatClientBuilder, DEFAULT_EVALUATION_PROMPT_TEXT);
    }

    public EnhancedRelevancyEvaluator(final ChatClient.Builder chatClientBuilder,
                                      final String evaluationPrompt) {
        this.chatClientBuilder = chatClientBuilder;
        this.evaluationPrompt = evaluationPrompt;
    }

    @Override
    public EvaluationResponse evaluate(final EvaluationRequest evaluationRequest) {

        var response = evaluationRequest.getResponseContent();
        var context = doGetSupportingData(evaluationRequest);

        final String evaluationResponse = this.chatClientBuilder.build()
                .prompt()
                .user(userSpec -> userSpec.text(evaluationPrompt)
                        .param("query", evaluationRequest.getUserText())
                        .param("response", response)
                        .param("context", context))
                .call()
                .content();

        boolean passing = false;

        if (evaluationResponse != null && !evaluationResponse.isBlank()) {
            passing = evaluationResponse.toLowerCase().contains("yes");
        }

        return new EvaluationResponse(passing, evaluationResponse, Collections.emptyMap());
    }
}
