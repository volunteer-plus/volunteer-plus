package volunteer.plus.backend.service.general.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.general.CookieService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public Optional<String> getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank())
                .map(Cookie::getValue)
                .findFirst();
    }

    @Override
    public void setCookie(HttpServletResponse response, String name, String value, Duration maxAge, boolean httpOnly, boolean secure, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(path);
        cookie.setMaxAge((int) maxAge.getSeconds());
        response.addCookie(cookie);
    }

    @Override
    public String getOrCreateCookie(HttpServletRequest request, HttpServletResponse response, String name, Supplier<String> valueGenerator, Duration maxAge, boolean httpOnly, boolean secure, String path) {
        return getCookieValue(request, name)
                .orElseGet(() -> {
            String value = valueGenerator.get();
            setCookie(response, name, value, maxAge, httpOnly, secure, path);
            return value;
        });
    }

}
