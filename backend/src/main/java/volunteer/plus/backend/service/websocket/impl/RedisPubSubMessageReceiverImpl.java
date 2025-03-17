package volunteer.plus.backend.service.websocket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.WebSocketMessageDTO;
import volunteer.plus.backend.service.websocket.RedisPubSubMessageReceiver;
import volunteer.plus.backend.util.JacksonUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubSubMessageReceiverImpl implements RedisPubSubMessageReceiver {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void receiveMessage(final String messagePayload) {
        final WebSocketMessageDTO message = JacksonUtil.deserialize(messagePayload, WebSocketMessageDTO.class);
        log.info("Send websocket message via Redis to topic: {}, with payload: {}", message.getTopic(), message.getPayload());
        messagingTemplate.convertAndSend(message.getTopic(), message.getPayload());
    }
}
