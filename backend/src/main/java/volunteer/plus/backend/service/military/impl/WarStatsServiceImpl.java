package volunteer.plus.backend.service.military.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import volunteer.plus.backend.domain.dto.ai.news.AINewsFeedResponse;
import volunteer.plus.backend.domain.dto.stats.WarStatsRangeResponseDTO;
import volunteer.plus.backend.domain.dto.stats.WarStatsResponseDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.service.general.NewsFeedService;
import volunteer.plus.backend.service.military.WarStatsService;
import volunteer.plus.backend.util.AIClientProviderUtil;
import volunteer.plus.backend.util.JacksonUtil;

import java.nio.charset.Charset;
import java.time.LocalDate;

@Slf4j
@Service
public class WarStatsServiceImpl implements WarStatsService {
    private final RestClient warStatsRestClient;
    private final AIClientProviderUtil aiClientProviderUtil;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;
    private final NewsFeedService newsFeedService;
    private final Resource warStatsNewsFeed;

    public WarStatsServiceImpl(final @Qualifier("warStatsRestClient") RestClient warStatsRestClient,
                               final AIClientProviderUtil aiClientProviderUtil,
                               final UserRepository userRepository,
                               final OpenAIService openAIService,
                               final NewsFeedService newsFeedService,
                               final @Value("classpath:/prompts/war_stats_news_feed.txt") Resource warStatsNewsFeed) {
        this.warStatsRestClient = warStatsRestClient;
        this.aiClientProviderUtil = aiClientProviderUtil;
        this.userRepository = userRepository;
        this.openAIService = openAIService;
        this.newsFeedService = newsFeedService;
        this.warStatsNewsFeed = warStatsNewsFeed;
    }

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

    @SneakyThrows
    @Override
    public void processStatsNotification(final AIChatClient aiChatClient) {
        final WarStatsResponseDTO statsResponseDTO = getWarStats(null);

        final ChatClient chatClient = aiClientProviderUtil.getChatClient(aiChatClient);

        final User user = userRepository.findUserByEmail(aiChatClient.getUserEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        log.info("Start of generation AI driven news feed...");

        final AINewsFeedResponse response = chatClient.prompt(warStatsNewsFeed.getContentAsString(Charset.defaultCharset()) + JacksonUtil.serialize(statsResponseDTO))
                .call()
                .entity(AINewsFeedResponse.class);

        newsFeedService.generateNewsAINewsFeed(user, response, newsFeedService, openAIService);
    }
}
