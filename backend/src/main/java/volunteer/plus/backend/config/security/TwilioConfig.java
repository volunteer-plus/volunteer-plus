package volunteer.plus.backend.config.security;

import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

//@Configuration
public class TwilioConfig {

    @Value(value = "${twilio.account-sid}")
    private String accountSid;
    @Value(value = "${twilio.auth-token}")
    private String authToken;

    @Bean
    public TwilioRestClient twilioRestClient() {
        return new TwilioRestClient.Builder(accountSid, authToken).build();
    }

}
