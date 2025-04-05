package volunteer.plus.backend.domain.dto.ai.agent;

public record EvaluationResponse(Evaluation evaluation, String feedback) {
    public enum Evaluation {
        PASS, NEEDS_IMPROVEMENT, FAIL
    }
}
