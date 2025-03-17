package volunteer.plus.backend.config.cache;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

// allows us to disable redis auto configs from app
@Configuration
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "true")
public class RedisDisableConfig {
}
