package volunteer.plus.backend.service.ai.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.ai.AIModerationService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
public class AIModerationServiceImpl implements AIModerationService {
    private final OpenAiModerationModel moderationModel;

    public AIModerationServiceImpl(OpenAiModerationModel moderationModel) {
        this.moderationModel = moderationModel;
    }

    @Override
    @Async("asyncExecutor")
    public Future<ModerationResponse> moderate(final String message) {
        log.info("Message moderation...");

        // Create a moderation prompt for the text
        final ModerationPrompt moderationPrompt = new ModerationPrompt(message);

        return CompletableFuture.completedFuture(moderationModel.call(moderationPrompt));
    }
}
