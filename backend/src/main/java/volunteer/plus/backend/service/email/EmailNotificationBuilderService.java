package volunteer.plus.backend.service.email;

import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.entity.Report;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VolunteerFeedback;

public interface EmailNotificationBuilderService {
    void createReportEmails(Report report);

    void createVolunteerFeedbackEmail(VolunteerFeedback feedback);

    void createUserRegistrationEmail(User user);

    void createUserPaymentEmail(LiqPayCreationDTO liqPayCreationDTO, User user);
}
