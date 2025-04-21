package volunteer.plus.backend.service.military.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import volunteer.plus.backend.domain.dto.stats.WarStatsRangeResponseDTO;
import volunteer.plus.backend.domain.dto.stats.WarStatsResponseDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.military.WarStatsService;

import java.time.LocalDate;

@Slf4j
@Service
public class WarStatsServiceImpl implements WarStatsService {
    private final RestClient warStatsRestClient;

    public WarStatsServiceImpl(final @Qualifier("warStatsRestClient") RestClient warStatsRestClient) {
        this.warStatsRestClient = warStatsRestClient;
    }

    @Tool(description = "Get General Staff war statistics for specific date")
    @Override
    @Retryable(retryFor = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public WarStatsResponseDTO getWarStats(final LocalDate date) {
        final String requestUri = date == null ? "/statistics/latest" : "/statistics/" + date;

        log.info("Getting war stats for {}", requestUri);

        try {
            return warStatsRestClient.get()
                    .uri(requestUri)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .body(WarStatsResponseDTO.class);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    @Tool(description = "Get General Staff war statistics in date range from and to")
    @Override
    @Retryable(retryFor = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public WarStatsRangeResponseDTO getWarStatsRange(final LocalDate dateFrom,
                                                     final LocalDate dateTo) {
        log.info("Getting war stats from {}, to {}", dateFrom, dateTo);
        try {
            return warStatsRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/statistics")
                            .queryParam("date_from", dateFrom)
                            .queryParam("date_to", dateTo).build()
                    )
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .body(WarStatsRangeResponseDTO.class);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
}
