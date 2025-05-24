package volunteer.plus.backend.service.auth;

public interface TotpService {

    String generateSecret();

    boolean verifyCode(String secret, String code);

    String getUri(String secret, String accountName, String issuer);
}
