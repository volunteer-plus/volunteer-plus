package volunteer.plus.backend.service.military.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.dto.ai.news.AINewsFeedResponse;
import volunteer.plus.backend.domain.dto.stats.WarStatsResponseDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.general.NewsFeedService;
import volunteer.plus.backend.service.military.WarStatsNotificationService;
import volunteer.plus.backend.service.military.WarStatsService;
import volunteer.plus.backend.util.AIClientProviderUtil;
import volunteer.plus.backend.util.JacksonUtil;

import java.nio.charset.Charset;

@Slf4j
@Service
public class WarStatsNotificationServiceImpl implements WarStatsNotificationService {
    private final WarStatsService warStatsService;
    private final AIClientProviderUtil aiClientProviderUtil;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;
    private final NewsFeedService newsFeedService;
    private final Resource warStatsNewsFeed;
    private final EmailNotificationBuilderService emailNotificationBuilderService;

    public WarStatsNotificationServiceImpl(final WarStatsService warStatsService,
                                           final AIClientProviderUtil aiClientProviderUtil,
                                           final UserRepository userRepository,
                                           final OpenAIService openAIService,
                                           final NewsFeedService newsFeedService,
                                           final @Value("classpath:/prompts/war_stats_news_feed.txt") Resource warStatsNewsFeed,
                                           final EmailNotificationBuilderService emailNotificationBuilderService) {
        this.warStatsService = warStatsService;
        this.aiClientProviderUtil = aiClientProviderUtil;
        this.userRepository = userRepository;
        this.openAIService = openAIService;
        this.newsFeedService = newsFeedService;
        this.warStatsNewsFeed = warStatsNewsFeed;
        this.emailNotificationBuilderService = emailNotificationBuilderService;
    }

    @Override
    @SneakyThrows
    public void processStatsNotification(final AIChatClient aiChatClient) {
        final WarStatsResponseDTO statsResponseDTO = warStatsService.getWarStats(null);

        final ChatClient chatClient = aiClientProviderUtil.getChatClient(aiChatClient);

        final User user = userRepository.findUserByEmail(aiChatClient.getUserEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        log.info("Start of generation AI driven news feed...");

        final AINewsFeedResponse response = chatClient.prompt(warStatsNewsFeed.getContentAsString(Charset.defaultCharset()) + JacksonUtil.serialize(statsResponseDTO))
                .call()
                .entity(AINewsFeedResponse.class);

        final NewsFeedDTO newsFeedDTO = newsFeedService.generateNewsAINewsFeed(user, response, newsFeedService, openAIService);

        emailNotificationBuilderService.createGeneralStaffEmailNotification(newsFeedDTO);
    }
}
