package volunteer.plus.backend.service.auth;

public interface Email2FaService {

    void send2faCode(String email, String code);
}
