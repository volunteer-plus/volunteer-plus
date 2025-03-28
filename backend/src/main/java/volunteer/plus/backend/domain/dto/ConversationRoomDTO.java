package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationRoomDTO {
    private Long id;
    @NotNull
    private String name;
    private boolean deleted;
    private List<WSMessageDTO> messages = new ArrayList<>();
    private List<UserConversationDTO> users = new ArrayList<>();
}
