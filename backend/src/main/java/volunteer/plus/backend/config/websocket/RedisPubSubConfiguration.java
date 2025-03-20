package volunteer.plus.backend.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import volunteer.plus.backend.service.websocket.RedisPubSubMessageReceiver;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
public class RedisPubSubConfiguration {
    public static final String PUB_SUB_CHANNEL = "pub-sub-channel";

    private final RedisConnectionFactory connectionFactory;
    private final RedisPubSubMessageReceiver pubSubMessageReceiver;

    @Bean
    public MessageListenerAdapter redisPubSubListenerAdapter() {
        return new MessageListenerAdapter(pubSubMessageReceiver, "receiveMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisPubSubContainer(final MessageListenerAdapter redisPubSubListenerAdapter) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisPubSubListenerAdapter, new PatternTopic(PUB_SUB_CHANNEL));
        return container;
    }
}
