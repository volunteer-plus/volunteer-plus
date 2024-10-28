package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private String subject;
    private String body;
    private List<FileAttachment> attachments = new ArrayList<>();
    private Set<EmailRecipientDTO> recipients = new HashSet<>();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileAttachment {
        private String name;
        private byte[] content;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailRecipientDTO {
        private String emailAddress;
        private boolean toRecipient;
        private boolean ccRecipient;
        private boolean bccRecipient;
    }
}
