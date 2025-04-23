package volunteer.plus.backend.service.alerts.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import volunteer.plus.backend.domain.dto.AlertRegionModelDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.alerts.AlertService;

import java.util.List;

@Slf4j
@Service
public class AlertServiceImpl implements AlertService {
    private final RestClient ukraineAlarmRestClient;

    public AlertServiceImpl(final @Qualifier("ukraineAlarmRestClient") RestClient ukraineAlarmRestClient) {
        this.ukraineAlarmRestClient = ukraineAlarmRestClient;
    }

    @Tool(description = "Get Ukraine Alarm alerts information")
    @Override
    @Retryable(retryFor = { Exception.class, ApiException.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public List<AlertRegionModelDTO> getAlerts() {
        log.info("Getting alarm information");
        try {
            return ukraineAlarmRestClient.get()
                    .uri("/alerts")
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
}
