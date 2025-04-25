package volunteer.plus.backend.service.alerts.impl;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.dto.ai.news.AINewsFeedResponse;
import volunteer.plus.backend.domain.dto.alarm.AlertDurationResponseDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.domain.enums.EmailMessageTag;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.service.alerts.AlarmNotificationService;
import volunteer.plus.backend.service.alerts.AlarmService;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.general.NewsFeedService;
import volunteer.plus.backend.service.websocket.WebSocketService;
import volunteer.plus.backend.util.AIClientProviderUtil;
import volunteer.plus.backend.util.JacksonUtil;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.ALARM_HISTORY_TARGET;

@Slf4j
@Service
public class AlarmNotificationServiceImpl implements AlarmNotificationService {
    private final AlarmService alarmService;
    private final AIClientProviderUtil aiClientProviderUtil;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;
    private final NewsFeedService newsFeedService;
    private final Resource alarmNewsFeedResource;
    private final EmailNotificationBuilderService emailNotificationBuilderService;
    private final WebSocketService webSocketService;

    public AlarmNotificationServiceImpl(final AlarmService alarmService,
                                        final AIClientProviderUtil aiClientProviderUtil,
                                        final UserRepository userRepository,
                                        final OpenAIService openAIService,
                                        final NewsFeedService newsFeedService,
                                        final @Value("classpath:/prompts/alarm_stats_news_feed.txt") Resource alarmNewsFeedResource,
                                        final EmailNotificationBuilderService emailNotificationBuilderService,
                                        final WebSocketService webSocketService) {
        this.alarmService = alarmService;
        this.aiClientProviderUtil = aiClientProviderUtil;
        this.userRepository = userRepository;
        this.openAIService = openAIService;
        this.newsFeedService = newsFeedService;
        this.alarmNewsFeedResource = alarmNewsFeedResource;
        this.emailNotificationBuilderService = emailNotificationBuilderService;
        this.webSocketService = webSocketService;
    }

    @Override
    @SneakyThrows
    @Transactional
    public void processAlarmHistoryNotification(final AIChatClient aiChatClient) {
        final List<AlertDurationResponseDTO> data = alarmService.getDateHistoryAlerts(LocalDate.now().minusDays(1L));

        final ChatClient chatClient = aiClientProviderUtil.getChatClient(aiChatClient);

        final User user = userRepository.findUserByEmail(aiChatClient.getUserEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        log.info("Start of generation AI driven news feed about alarm statistics...");

        final AINewsFeedResponse response = chatClient.prompt(alarmNewsFeedResource.getContentAsString(Charset.defaultCharset()) + JacksonUtil.serialize(data))
                .call()
                .entity(AINewsFeedResponse.class);

        final NewsFeedDTO newsFeedDTO = newsFeedService.generateNewsAINewsFeed(user, response, newsFeedService, openAIService);

        emailNotificationBuilderService.createNewsFeedAIEmailNotification(newsFeedDTO, EmailMessageTag.EMAIL_MESSAGE_TAG_7);

        webSocketService.sendNotification(ALARM_HISTORY_TARGET, response.getBody());
    }
}
