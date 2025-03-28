package volunteer.plus.backend.service.websocket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.WSChatMessageDTO;
import volunteer.plus.backend.domain.entity.ConversationRoom;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.WSMessage;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.ConversationRoomRepository;
import volunteer.plus.backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ConversationRoomRepository conversationRoomRepository;

    @Override
    @Transactional
    public void sendMessageToConvId(final WSChatMessageDTO chatMessage,
                                    final Long conversationRoomId) {
        final User userDetails = userRepository.findById(chatMessage.getSenderId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        final ConversationRoom conversationRoom = conversationRoomRepository.findByIdAndDeletedFalse(conversationRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CONVERSATION_ROOM_NOT_FOUND));

        final WSMessage wsMessage = WSMessage.builder()
                .content(chatMessage.getContent())
                .fromUser(userDetails.getId())
                .build();

        final WSMessage savedMessage = wsMessageRepository.save(wsMessage);

        conversationRoom.addMessage(savedMessage);

        conversationRoomRepository.save(conversationRoom);

        webSocketService.sendNotification(WS_DESTINATION_PREFIX + "/" + conversationRoomId, savedMessage);
    }
}
