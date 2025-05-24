package volunteer.plus.backend.service.auth;

public interface SmsService {

    void send(String phoneNumber, String message);
}
