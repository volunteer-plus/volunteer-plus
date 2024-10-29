package volunteer.plus.backend.service.email;

import volunteer.plus.backend.domain.entity.Report;

public interface EmailNotificationBuilderService {
    void createReportEmails(Report report);
}
