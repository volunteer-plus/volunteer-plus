package volunteer.plus.backend.service.websocket.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.WebSocketMessageDTO;
import volunteer.plus.backend.service.websocket.WebSocketService;
import volunteer.plus.backend.util.JacksonUtil;

import static volunteer.plus.backend.config.websocket.RedisPubSubConfiguration.PUB_SUB_CHANNEL;

@Slf4j
@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketServiceImpl(@Autowired(required = false) final StringRedisTemplate redisTemplate,
                                final SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(final String destination,
                                 final Object payload) {

        // Redis is used to distributes messages between all application clusters
        // can be used there any other message broker but since we already leverage on Redis we used it
        if (redisTemplate != null) {
            log.info("Start sending notification to WS via Redis: {}", destination);

            final String data = JacksonUtil.serialize(
                    WebSocketMessageDTO.builder()
                            .topic(destination)
                            .payload(payload)
                            .build()
            );

            redisTemplate.convertAndSend(PUB_SUB_CHANNEL, data);
        } else {
            log.info("Start sending notification to WS directly destination: {}, with payload: {}", destination, payload);
            messagingTemplate.convertAndSend(destination, payload);
        }
    }
}
