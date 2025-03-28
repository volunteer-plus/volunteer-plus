package volunteer.plus.backend.service.websocket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.WSChatMessageDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.WSMessage;
import volunteer.plus.backend.repository.WSMessageRepository;
import volunteer.plus.backend.service.websocket.WSChatService;
import volunteer.plus.backend.service.websocket.WebSocketService;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.WS_DESTINATION_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class WSChatServiceImpl implements WSChatService {

    private final WebSocketService webSocketService;
    private final WSMessageRepository wsMessageRepository;

    @Override
    @Transactional
    public void sendMessageToConvId(final WSChatMessageDTO chatMessage,
                                    final String conversationId,
                                    final SimpMessageHeaderAccessor headerAccessor) {
        final User userDetails = getUser();

        if (userDetails != null) {
            populateContext(chatMessage, userDetails);
        }

        final Long fromUserId = userDetails == null ? null : userDetails.getId();
        final Long toUserId = chatMessage.getReceiverId();

        final WSMessage wsMessage = WSMessage.builder()
                .fromUser(fromUserId)
                .toUser(toUserId)
                .content(chatMessage.getContent())
                .convId(conversationId)
                .build();

        final WSMessage savedMessage = wsMessageRepository.save(wsMessage);

        webSocketService.sendNotification(WS_DESTINATION_PREFIX + "/" + conversationId, savedMessage);
    }

    private void populateContext(final WSChatMessageDTO chatMessage,
                                 final User userDetails) {
        chatMessage.setSenderUsername(userDetails.getUsername());
        chatMessage.setSenderId(userDetails.getId());
    }

    public User getUser() {
        final Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        return null;
    }
}
