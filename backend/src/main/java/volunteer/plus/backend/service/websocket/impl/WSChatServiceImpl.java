package volunteer.plus.backend.service.websocket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.WSChatMessageDTO;
import volunteer.plus.backend.domain.entity.ConversationRoom;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.WSMessage;
import volunteer.plus.backend.domain.enums.AIChatClient;
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

        final WSMessage wsMessage = WSMessage.builder()
                .content(chatMessage.getContent())
                .fromUser(userDetails.getId())
                .build();

        final WSMessage savedMessage = wsMessageRepository.save(wsMessage);

        conversationRoom.addMessage(savedMessage);

        conversationRoomRepository.save(conversationRoom);

        switch (chatMessage.getAiChat()) {
            case OLLAMA -> {
                final String result = ollamaAIService.chat(AIChatClient.OLLAMA_DEFAULT, OllamaAIModel.TINY_LLAMA, chatMessage.getContent(), List.of()).getChatResponse();
                webSocketService.sendNotification(OLLAMA_RESPONSE_TARGET, result);
            }
            case OPENAI -> {
                final String result = openAIService.chat(AIChatClient.OPENAI_DEFAULT, chatMessage.getContent(), List.of()).getChatResponse();
                webSocketService.sendNotification(OPENAI_RESPONSE_TARGET, result);
            }
            case null, default -> webSocketService.sendNotification(WS_DESTINATION_PREFIX + "/" + conversationRoomId, chatMessage.getContent());
        }

    }
}
