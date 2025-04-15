package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.AIChatClient;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSChatMessageDTO {
    @NotBlank
    private String content;

    @NotNull
    private Long senderId;

    private AIChatClient aiChat;
}
