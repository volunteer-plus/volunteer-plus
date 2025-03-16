package volunteer.plus.backend.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {
    public static final Integer TTL_REDIS_CACHE_MINUTES = 60;

    public static final String REDIS_BROKER_CODES_CACHE_KEY = "'brigadeCodesKey'";
    public static final String REDIS_BROKER_CODES_CACHE_NAMES = "brigadeCodes";
    public static final String REDIS_BRIGADES_CACHE_NAMES = "brigades";

    private final ObjectMapper objectMapper;

    public RedisCacheConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Use a String serializer for the keys
        template.setKeySerializer(new StringRedisSerializer());
        // Use a JSON serializer for the values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(TTL_REDIS_CACHE_MINUTES));
    }

    @Bean
    public RedisCacheConfiguration redisCacheObjectConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(TTL_REDIS_CACHE_MINUTES))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
                );
    }

    @Bean
    public CacheManager redisCacheManager(final RedisConnectionFactory connectionFactory,
                                          final RedisCacheConfiguration redisCacheConfiguration,
                                          final RedisCacheConfiguration redisCacheObjectConfiguration) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .withCacheConfiguration(REDIS_BROKER_CODES_CACHE_NAMES, redisCacheConfiguration)
                .withCacheConfiguration(REDIS_BRIGADES_CACHE_NAMES, redisCacheObjectConfiguration)
                .build();
    }
}
