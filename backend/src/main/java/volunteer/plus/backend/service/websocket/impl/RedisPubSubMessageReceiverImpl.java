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
        log.info("Send websocket message: {}", messagePayload);
        final WebSocketMessageDTO message = JacksonUtil.deserialize(messagePayload, WebSocketMessageDTO.class);
        messagingTemplate.convertAndSend(message.getTopic(), message.getPayload());
    }
}
