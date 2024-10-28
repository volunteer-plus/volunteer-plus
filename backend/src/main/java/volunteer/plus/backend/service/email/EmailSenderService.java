package volunteer.plus.backend.service.email;

import volunteer.plus.backend.domain.dto.EmailDTO;

public interface EmailSenderService {
    void sendEmail(EmailDTO emailDTO);
}
