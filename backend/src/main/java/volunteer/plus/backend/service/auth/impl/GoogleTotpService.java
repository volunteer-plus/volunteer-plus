package volunteer.plus.backend.service.auth.impl;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.auth.TotpService;

@Service
public class GoogleTotpService implements TotpService {

    private final GoogleAuthenticator gAuth;

    public GoogleTotpService() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(30000)
                .setWindowSize(1)
                .build();
        this.gAuth = new GoogleAuthenticator(config);
    }

    @Override
    public String generateSecret() {
        return gAuth.createCredentials().getKey();
    }

    @Override
    public boolean verifyCode(String secret, String code) {
        try {
            int codeInt = Integer.parseInt(code);
            return gAuth.authorize(secret, codeInt);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getUri(String secret, String accountName, String issuer) {
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey.Builder(secret).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, accountName, key);
    }
}
