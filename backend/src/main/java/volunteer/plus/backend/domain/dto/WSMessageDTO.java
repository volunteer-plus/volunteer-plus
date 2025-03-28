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
public class WSMessageDTO {
    private Long id;
    private String content;
    private Long fromUser;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
