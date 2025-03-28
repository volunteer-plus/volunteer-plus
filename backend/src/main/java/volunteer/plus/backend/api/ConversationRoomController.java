package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.ConversationRoomDTO;
import volunteer.plus.backend.service.general.ConversationRoomService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConversationRoomController {
    private final ConversationRoomService conversationRoomService;

    @GetMapping("/rooms")
    @Operation(description = "Retrieve conversation rooms")
    public List<ConversationRoomDTO> findConversationRooms(@RequestParam(required = false) final Long userId) {
        return conversationRoomService.findConversationRooms(userId);
    }

    @PostMapping("/room/create")
    @Operation(description = "Create conversation rooms")
    public ConversationRoomDTO createConversationRoom(@RequestBody @Valid final ConversationRoomDTO conversationRoomDTO) {
        return conversationRoomService.createConversationRoom(conversationRoomDTO);
    }

    @PostMapping("/room/add-user")
    @Operation(description = "Add user to conversation rooms")
    public ConversationRoomDTO addUserToConversationRoom(@RequestParam final Long conversationRoomId,
                                                         @RequestParam final Long userId) {
        return conversationRoomService.addUserToConversationRoom(conversationRoomId, userId);
    }

    @PutMapping("/room/remove-user")
    @Operation(description = "Remove user from conversation rooms")
    public ConversationRoomDTO removeUserFromConversationRoom(@RequestParam final Long conversationRoomId,
                                                              @RequestParam final Long userId) {
        return conversationRoomService.removeUserFromConversationRoom(conversationRoomId, userId);
    }

    @DeleteMapping("/room/delete")
    @Operation(description = "Delete conversation room")
    public void deleteConversationRoom(@RequestParam final Long conversationRoomId) {
        conversationRoomService.deleteConversationRoom(conversationRoomId);
    }
}
