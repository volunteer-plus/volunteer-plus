package volunteer.plus.backend.service.alerts.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import volunteer.plus.backend.domain.dto.AlertDurationResponseDTO;
import volunteer.plus.backend.domain.dto.AlertRegionModelDTO;
import volunteer.plus.backend.domain.dto.RegionAlarmsHistoryDTO;
import volunteer.plus.backend.domain.dto.RegionsViewModelDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.alerts.AlarmService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private final RestClient ukraineAlarmRestClient;

    public AlarmServiceImpl(final @Qualifier("ukraineAlarmRestClient") RestClient ukraineAlarmRestClient) {
        this.ukraineAlarmRestClient = ukraineAlarmRestClient;
    }

    @Tool(description = "Get Ukraine Alarm alerts information")
    @Override
    @Retryable(retryFor = { Exception.class, ApiException.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public List<AlertRegionModelDTO> getAlerts(final String regionId) {
        final String uri = regionId == null ? "/alerts" : "/alerts/" + regionId;

        log.info("Getting alarm information for {}", uri);

        try {
            return ukraineAlarmRestClient.get()
                    .uri(uri)
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    @Tool(description = "Get Ukraine regions")
    @Override
    @Retryable(retryFor = { Exception.class, ApiException.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public RegionsViewModelDTO getRegions() {
        log.info("Getting Ukraine regions");

        try {
            return ukraineAlarmRestClient.get()
                    .uri("/regions")
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .retrieve()
                    .body(RegionsViewModelDTO.class);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    @Tool(description = "Get Ukraine Alarm alerts date history information")
    @Override
    @Retryable(retryFor = { Exception.class, ApiException.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public List<AlertDurationResponseDTO> getDateHistoryAlerts(final LocalDate date) {
        log.info("Getting alarm history information for {}", date);

        try {
            return ukraineAlarmRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/alerts/dateHistory")
                            .queryParam("date", date.toString().replace("-", ""))
                            .build())
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    @Tool(description = "Get Ukraine Alarm alerts region history alerts information")
    @Override
    @Retryable(retryFor = { Exception.class, ApiException.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public List<RegionAlarmsHistoryDTO> getRegionHistoryAlerts(final String regionId) {
        log.info("Getting alarm region history information: {}", regionId);

        try {
            return ukraineAlarmRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/alerts/regionHistory")
                            .queryParam("regionId", regionId)
                            .build())
                    .header(CONTENT_TYPE, APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
}
