package volunteer.plus.backend.service.email;

public interface EmailContentEvaluationService {
    String evaluateContent(String html, String data);
}
