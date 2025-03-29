package volunteer.plus.backend.service.general;


import volunteer.plus.backend.domain.dto.ConversationRoomDTO;

import java.util.List;

public interface ConversationRoomService {
    List<ConversationRoomDTO> findConversationRooms(Long userId);

    ConversationRoomDTO getConversationRoom(Long conversationRoomId);

    ConversationRoomDTO createConversationRoom(ConversationRoomDTO conversationRoomDTO);

    ConversationRoomDTO addUserToConversationRoom(Long conversationRoomId, Long userId);

    ConversationRoomDTO removeUserFromConversationRoom(Long conversationRoomId, Long userId);

    void deleteConversationRoom(Long conversationRoomId);

    void deleteConversationRoomMessage(Long conversationRoomId, Long messageId);
}
