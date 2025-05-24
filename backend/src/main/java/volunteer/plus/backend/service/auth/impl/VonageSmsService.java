package volunteer.plus.backend.service.auth.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.auth.SmsService;

@Service
@Slf4j
@RequiredArgsConstructor
public class VonageSmsService implements SmsService {

    private static final Logger log = LoggerFactory.getLogger(VonageSmsService.class);
    @Value("${VONAGE_ACCOUNT_API_KEY}")
    private String API_KEY;

    @Value("${VONAGE_API_SECRET}")
    private String API_SECRET;


    @Override
    public void send(String phoneNumber, String messageText) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number must be provided");
        }

        try {
            VonageClient client = VonageClient.builder()
                    .apiKey(API_KEY)
                    .apiSecret(API_SECRET)
                    .build();

            TextMessage message = new TextMessage(
                    "Volonter (+)",
                    phoneNumber,
                    messageText
            );

            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                log.info("✅ SMS sent successfully to {}", phoneNumber);
            } else {
                String errorText = response.getMessages().get(0).getErrorText();
                log.error("❌ Failed to send SMS to {}: {}", phoneNumber, errorText);
                throw new RuntimeException("Failed to send SMS: " + errorText);
            }

        } catch (Exception e) {
            log.error("❌ Error sending SMS", e);
            throw new RuntimeException("Exception occurred while sending SMS", e);
        }
    }
}
