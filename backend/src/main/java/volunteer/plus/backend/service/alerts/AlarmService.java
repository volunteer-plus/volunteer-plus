package volunteer.plus.backend.service.alerts;

import volunteer.plus.backend.domain.dto.alarm.*;

import java.time.LocalDate;
import java.util.List;

public interface AlarmService {
    List<AlertRegionModelDTO> getAlerts(String regionId);

    RegionsViewModelDTO getRegions();

    List<AlertDurationResponseDTO> getDateHistoryAlerts(LocalDate date);

    List<RegionAlarmsHistoryDTO> getRegionHistoryAlerts(String regionId);

    void subscribeToWebhook(WebHookModelDTO webHookModelDTO);

    void updateSubscriptionToWebhook(WebHookModelDTO webHookModelDTO);

    void unsubscribeFromWebhook(WebHookModelDTO webHookModelDTO);

    void handleAlert(AlertRegionModelDTO alert);
}
