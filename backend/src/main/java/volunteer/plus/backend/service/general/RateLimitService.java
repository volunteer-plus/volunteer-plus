package volunteer.plus.backend.service.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RateLimitService {

    String resolveKey(HttpServletRequest request, HttpServletResponse response);

}
