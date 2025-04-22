package volunteer.plus.backend.service.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

public interface CookieService {

    Optional<String> getCookieValue(HttpServletRequest request, String name);

    void setCookie(HttpServletResponse response, String name, String value, Duration maxAge, boolean httpOnly, boolean secure, String path);

    String getOrCreateCookie(HttpServletRequest request, HttpServletResponse response, String name, Supplier<String> valueGenerator, Duration maxAge, boolean httpOnly, boolean secure, String path);

}
