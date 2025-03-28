package volunteer.plus.backend.service.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.security.JwtTokenExtractor;

@Service
public class JwtTokenExtractorImpl implements JwtTokenExtractor {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public String extract(HttpServletRequest request) {
        var header = request.getHeader(AUTH_HEADER);
        if (!StringUtil.isBlank(header) && header.startsWith(BEARER_PREFIX)) {
            return request.getHeader(AUTH_HEADER).substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
