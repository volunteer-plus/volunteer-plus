package volunteer.plus.backend.service.alerts;

import volunteer.plus.backend.domain.enums.AIChatClient;

public interface AlarmNotificationService {
    void processAlarmHistoryNotification(AIChatClient aiChatClient);
}
