package volunteer.plus.backend.service.websocket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.WSChatMessageDTO;
import volunteer.plus.backend.domain.entity.ConversationRoom;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.WSMessage;
import volunteer.plus.backend.domain.enums.OllamaAIModel;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.ConversationRoomRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.repository.WSMessageRepository;
import volunteer.plus.backend.service.ai.OllamaAIService;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.service.websocket.WSChatService;
import volunteer.plus.backend.service.websocket.WebSocketService;

import java.util.List;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class WSChatServiceImpl implements WSChatService {
    private final OllamaAIService ollamaAIService;
    private final OpenAIService openAIService;
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

        final WSMessage savedMessage = getSavedMessage(chatMessage.getContent(), userDetails);
        conversationRoom.addMessage(savedMessage);

        // in case it is not AI related message we should save it and send to topic without waiting for response
        if (chatMessage.getAiChat() == null) {
            conversationRoomRepository.save(conversationRoom);
            webSocketService.sendNotification(WS_DESTINATION_PREFIX + "/" + conversationRoomId, chatMessage.getContent());
            return;
        }

        final User aiUser = userRepository.findUserByEmail(chatMessage.getAiChat().getUserEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        final String response = switch (chatMessage.getAiChat().getGeneralChat()) {
            case OLLAMA -> ollamaAIService.chat(chatMessage.getAiChat(), OllamaAIModel.TINY_LLAMA, chatMessage.getContent(), List.of()).getChatResponse();
            case OPENAI -> openAIService.chat(chatMessage.getAiChat(), chatMessage.getContent(), List.of()).getChatResponse();
        };

        final WSMessage savedAIResponseMessage = getSavedMessage(response, aiUser);
        conversationRoom.addMessage(savedAIResponseMessage);

        conversationRoomRepository.save(conversationRoom);

        webSocketService.sendNotification(WS_DESTINATION_PREFIX + "/" + conversationRoomId, response);
    }

    private WSMessage getSavedMessage(final String content,
                                      final User user) {
        final WSMessage wsMessage = WSMessage.builder()
                .content(content)
                .fromUser(user.getId())
                .build();

        return wsMessageRepository.save(wsMessage);
    }
}
