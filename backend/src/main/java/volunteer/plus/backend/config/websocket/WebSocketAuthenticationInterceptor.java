package volunteer.plus.backend.config.websocket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.entity.User;

import java.nio.file.AccessDeniedException;

@Slf4j
@Component
public class WebSocketAuthenticationInterceptor implements ChannelInterceptor {
    private final boolean websocketSkipAuth;

    public WebSocketAuthenticationInterceptor(@Value("${websocket.skip.auth}") boolean websocketSkipAuth) {
        this.websocketSkipAuth = websocketSkipAuth;
    }

    @SneakyThrows
    @Override
    public Message<?> preSend(final Message<?> message,
                              final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && !websocketSkipAuth && (accessor.getCommand() == StompCommand.CONNECT || accessor.getCommand() == StompCommand.SEND)) {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User user) {
                // in case of empty authorities user cannot perform WebSocket operation
                if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
                    throw new AccessDeniedException("WebSocket Access has been denied!");
                }
            } else {
                throw new AccessDeniedException("WebSocket Access has been denied!");
            }
        }

        return message;
    }
}
