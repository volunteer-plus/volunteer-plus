package volunteer.plus.backend.service.websocket;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import volunteer.plus.backend.domain.dto.WSChatMessageDTO;

public interface WSChatService {
    void sendMessageToConvId(WSChatMessageDTO wsChatMessageDTO,
                             String conversationId,
                             SimpMessageHeaderAccessor headerAccessor);
}
