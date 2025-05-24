package volunteer.plus.backend.service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RateLimitService {

    String resolveKey(HttpServletRequest request, HttpServletResponse response);

}
