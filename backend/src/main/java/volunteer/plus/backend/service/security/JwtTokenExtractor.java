package volunteer.plus.backend.service.security;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenExtractor {

    String extract(HttpServletRequest request);
}
