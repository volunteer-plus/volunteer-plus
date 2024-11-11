package volunteer.plus.backend.service.email;

import volunteer.plus.backend.domain.entity.Report;
import volunteer.plus.backend.domain.entity.VolunteerFeedback;

public interface EmailNotificationBuilderService {
    void createReportEmails(Report report);

    void createVolunteerFeedbackEmail(VolunteerFeedback feedback);
}
