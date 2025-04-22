package volunteer.plus.backend.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static volunteer.plus.backend.util.CacheUtil.*;

@Configuration
public class CaffeineConfig {

    private final Integer brigadeCodesTtl;
    private final Integer brigadesTtl;
    private final Integer rateLimitTtl;

    public CaffeineConfig(@Value("${spring.caffeine.ttl.brigade-codes}") final Integer brigadeCodesTtl,
                          @Value("${spring.caffeine.ttl.brigades}") final Integer brigadesTtl,
                          @Value("${spring.caffeine.ttl.rate-limit-buckets}") final Integer rateLimitTtl) {
        this.brigadeCodesTtl = brigadeCodesTtl;
        this.brigadesTtl = brigadesTtl;
        this.rateLimitTtl = rateLimitTtl;
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        final CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCacheNames(List.of(BRIGADE_CODES_CACHE, BRIGADES_CACHE, RATE_LIMIT_CACHE));

        final SimpleCacheManager manager = new SimpleCacheManager();
        final CaffeineCache brigadeCodesCache = buildCache(BRIGADE_CODES_CACHE, brigadeCodesTtl);
        final CaffeineCache brigadesCache = buildCache(BRIGADES_CACHE, brigadesTtl);
        final CaffeineCache rateLimitCache = buildCache(RATE_LIMIT_CACHE, rateLimitTtl);

        manager.setCaches(List.of(brigadeCodesCache, brigadesCache, rateLimitCache));

        return caffeineCacheManager;
    }

    public CaffeineCache buildCache(final String name,
                                    final Integer ttl) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(ttl, TimeUnit.MINUTES)
                .build());
    }
}
