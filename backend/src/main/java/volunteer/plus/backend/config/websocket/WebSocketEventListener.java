package volunteer.plus.backend.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class WebSocketEventListener {

    public static final String SIMP_DESTINATION = "simpDestination";
    public static final String SIMP_SESSION_ID = "simpSessionId";
    private final Map<String, String> simpSessionIdToSubscriptionId = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
        final String simpSessionId = (String) event.getMessage()
                .getHeaders()
                .get(SIMP_SESSION_ID);

        simpSessionIdToSubscriptionId.remove(simpSessionId);
    }

    @EventListener
    @SendToUser
    public void handleSubscribeEvent(final SessionSubscribeEvent sessionSubscribeEvent) {
        final String subscribedChannel = (String) sessionSubscribeEvent.getMessage()
                .getHeaders()
                .get(SIMP_DESTINATION);

        final String simpSessionId = (String) sessionSubscribeEvent.getMessage()
                .getHeaders()
                .get(SIMP_SESSION_ID);

        simpSessionIdToSubscriptionId.put(simpSessionId, subscribedChannel);
    }
}
