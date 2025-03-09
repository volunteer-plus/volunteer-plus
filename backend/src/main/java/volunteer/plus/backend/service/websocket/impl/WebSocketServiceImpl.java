package volunteer.plus.backend.service.websocket.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.websocket.WebSocketService;

@Slf4j
@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(final String destination,
                                 final Object payload) {

        log.info("Start sending notification to destination: {}", destination);

        messagingTemplate.convertAndSend(destination, payload);

        log.info("Finished sending notification to destination: {}", destination);
    }
}
