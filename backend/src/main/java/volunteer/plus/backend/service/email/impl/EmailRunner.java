package volunteer.plus.backend.service.email.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.email.EmailNotificationService;

@Service
@RequiredArgsConstructor
public class EmailRunner {
    private final EmailNotificationService emailNotificationService;

    @Scheduled(cron = "${email.processing.cron}")
    public void run() {
        emailNotificationService.processEmailNotifications();
    }
}
