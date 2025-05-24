package volunteer.plus.backend.service.email;

import org.springframework.ai.chat.client.ChatClient;
import volunteer.plus.backend.domain.dto.PaymentCreationDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.entity.Report;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VolunteerFeedback;
import volunteer.plus.backend.domain.enums.EmailMessageTag;

import java.util.List;

public interface EmailNotificationBuilderService {
    void createReportEmails(Report report);

    void createReportAnalysisEmails(ChatClient chatClient, List<Report> reports);

    void createVolunteerFeedbackEmail(VolunteerFeedback feedback);

    void createUserRegistrationEmail(User user);

    void createUserPaymentEmail(PaymentCreationDTO paymentCreationDTO, User user);

    void createNewsFeedAIEmailNotification(NewsFeedDTO newsFeedDTO, final EmailMessageTag messageTag);
}
