package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsFeedCommentDTO {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @NotNull
    @NotBlank
    private String body;
    private String authorEmail;
    private String authorFirstName;
    private String authorLastName;
}
