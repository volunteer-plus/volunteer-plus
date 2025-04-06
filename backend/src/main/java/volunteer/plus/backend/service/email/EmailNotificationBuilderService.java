package volunteer.plus.backend.service.email;

import org.springframework.ai.chat.client.ChatClient;
import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.entity.Report;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VolunteerFeedback;

import java.util.List;

public interface EmailNotificationBuilderService {
    void createReportEmails(Report report);

    void createReportAnalysisEmails(ChatClient chatClient, List<Report> reports);

    void createVolunteerFeedbackEmail(VolunteerFeedback feedback);

    void createUserRegistrationEmail(User user);

    void createUserPaymentEmail(LiqPayCreationDTO liqPayCreationDTO, User user);
}
