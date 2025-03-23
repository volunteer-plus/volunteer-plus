package volunteer.plus.backend.service.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.security.JwtTokenExtractor;

@Service
public class JwtTokenExtractorImpl implements JwtTokenExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (!StringUtil.isBlank(header) && header.startsWith("Bearer ")) {
            return request.getHeader("Authorization");
        }

        return null;
    }
}
