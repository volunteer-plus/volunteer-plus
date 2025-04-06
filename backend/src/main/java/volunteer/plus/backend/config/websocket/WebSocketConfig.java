package volunteer.plus.backend.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String WS_ENDPOINT = "/ws-endpoint";
    public static final String WS_DESTINATION_PREFIX = "/topic";
    public static final String APP_MAPPING_PREFIX = "/app";

    public static final String OLLAMA_MESSAGE_MAPPING = "/ollama-message";
    public static final String OPENAI_MESSAGE_MAPPING = "/openai-message";
    public static final String CHAT_MESSAGE_MAPPING = "/chat/sendMessage/{conversationRoomId}";

    public static final String OLLAMA_RESPONSE_TARGET = WS_DESTINATION_PREFIX + "/ollama-response";
    public static final String OLLAMA_CHAT_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/ollama-chat-client";

    public static final String OPENAI_RESPONSE_TARGET = WS_DESTINATION_PREFIX + "/openai-response";
    public static final String OPENAI_CHAT_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-chat-client";
    public static final String OPENAI_IMAGE_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-image-client";
    public static final String OPENAI_TEXT_TO_SPEECH_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-text-to-speech-client";
    public static final String OPENAI_SPEECH_TO_TEXT_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-speech-to-text-client";

    private final WebSocketAuthenticationInterceptor authenticationInterceptor;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public WebSocketConfig(@Autowired(required = false) final WebSocketAuthenticationInterceptor authenticationInterceptor,
                           @Qualifier("wsTaskScheduler") final ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WS_ENDPOINT)
                .setAllowedOriginPatterns("*");

        registry.addEndpoint(WS_ENDPOINT)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .setApplicationDestinationPrefixes(APP_MAPPING_PREFIX)
                .enableSimpleBroker(WS_DESTINATION_PREFIX)
                .setTaskScheduler(threadPoolTaskScheduler)
                .setHeartbeatValue(new long[] {10000L, 10000L});
    }

    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        if (authenticationInterceptor != null) {
            registration.interceptors(authenticationInterceptor);
        }
    }
}
