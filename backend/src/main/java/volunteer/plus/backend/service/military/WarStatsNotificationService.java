package volunteer.plus.backend.service.military;

import volunteer.plus.backend.domain.enums.AIChatClient;

public interface WarStatsNotificationService {
    void processStatsNotification(AIChatClient aiChatClient);
}
