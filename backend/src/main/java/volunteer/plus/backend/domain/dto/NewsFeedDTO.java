package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.NewsFeedGenerationSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  NewsFeedDTO {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @NotNull
    @NotBlank
    private String subject;
    @NotNull
    @NotBlank
    private String body;
    private NewsFeedGenerationSource generationSource;
    private String authorEmail;
    private String authorFirstName;
    private String authorLastName;
    private List<NewsFeedCommentDTO> comments = new ArrayList<>();
    private List<NewsFeedAttachmentDTO> attachments = new ArrayList<>();
}
