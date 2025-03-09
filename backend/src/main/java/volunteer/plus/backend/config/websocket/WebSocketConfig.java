package volunteer.plus.backend.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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

    public static final String OLLAMA_RESPONSE_TARGET = WS_DESTINATION_PREFIX + "/ollama-response";
    public static final String OLLAMA_CHAT_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/ollama-chat-client";

    public static final String OPENAI_RESPONSE_TARGET = WS_DESTINATION_PREFIX + "/openai-response";
    public static final String OPENAI_CHAT_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-chat-client";
    public static final String OPENAI_IMAGE_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-image-client";
    public static final String OPENAI_TEXT_TO_SPEECH_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-text-to-speech-client";
    public static final String OPENAI_SPEECH_TO_TEXT_CLIENT_TARGET = WS_DESTINATION_PREFIX + "/openai-speech-to-text-client";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WS_ENDPOINT);

        registry.addEndpoint(WS_ENDPOINT)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(WS_DESTINATION_PREFIX);
        registry.setApplicationDestinationPrefixes(APP_MAPPING_PREFIX);
    }
}
