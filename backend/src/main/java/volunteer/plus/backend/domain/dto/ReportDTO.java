package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String data;

    private boolean analyzed;

    private List<AttachmentDTO> attachments = new ArrayList<>();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttachmentDTO {
        private Long id;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;

        private String filename;

        private String filepath;
    }
}
