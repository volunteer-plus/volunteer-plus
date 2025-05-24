package volunteer.plus.backend.service.security.impl;

import io.github.bucket4j.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import volunteer.plus.backend.service.security.RateLimitService;

import java.io.IOException;
import java.time.Duration;

import static volunteer.plus.backend.util.CacheUtil.RATE_LIMIT_CACHE;

@Component
public class RateLimitCacheFilter extends OncePerRequestFilter {

    private final Cache rateLimitCache;
    private final RateLimitService keyService;

    public RateLimitCacheFilter(CacheManager cacheManager,
                                RateLimitService keyService) {
        this.rateLimitCache = cacheManager.getCache(RATE_LIMIT_CACHE);
        this.keyService = keyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {
        String key = keyService.resolveKey(req, res);

        Bucket bucket = rateLimitCache.get(key, Bucket.class);
        if (bucket == null) {
            bucket = Bucket4j.builder()
                    .addLimit(Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1))))
                    .build();
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            rateLimitCache.put(key, bucket);
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.getWriter().write("Too many requests. Retry in " +
                    probe.getNanosToWaitForRefill() / 1_000_000_000 + "s");
        }
    }
}
