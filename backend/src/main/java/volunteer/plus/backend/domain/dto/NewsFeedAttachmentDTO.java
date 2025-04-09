package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsFeedAttachmentDTO {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String s3Link;
    private String filename;
    private boolean logo;
}
