package volunteer.plus.backend.service.websocket;

import volunteer.plus.backend.domain.dto.WSChatMessageDTO;

public interface WSChatService {
    void sendMessageToConvId(WSChatMessageDTO wsChatMessageDTO, Long conversationId);
}
