package volunteer.plus.backend.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

import static volunteer.plus.backend.util.CacheUtil.BRIGADES_CACHE;
import static volunteer.plus.backend.util.CacheUtil.BRIGADE_CODES_CACHE;

@Configuration
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
public class RedisCacheConfig {

    private final Integer brokerCodesTtl;
    private final Integer brigadesTtl;

    public RedisCacheConfig(@Value("${spring.cache.redis.broker-codes-ttl}") final Integer brokerCodesTtl,
                            @Value("${spring.cache.redis.brigades-ttl}") final Integer brigadesTtl) {
        this.brokerCodesTtl = brokerCodesTtl;
        this.brigadesTtl = brigadesTtl;
    }

    @Bean(name = "redisCacheManager")
    public CacheManager redisCacheManager(final RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .withCacheConfiguration(BRIGADE_CODES_CACHE, buildCacheConfiguration(brokerCodesTtl))
                .withCacheConfiguration(BRIGADES_CACHE, buildCacheConfiguration(brigadesTtl))
                .build();
    }

    private RedisCacheConfiguration buildCacheConfiguration(final Integer ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(ttl))
                .computePrefixWith(CacheKeyPrefix.simple());
    }
}
