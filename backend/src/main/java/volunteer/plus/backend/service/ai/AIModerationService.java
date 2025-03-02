package volunteer.plus.backend.service.ai;

import org.springframework.ai.moderation.ModerationResponse;

import java.util.concurrent.Future;

public interface AIModerationService {
    Future<ModerationResponse> moderate(String message);
}
