package volunteer.plus.backend.service.websocket;

public interface RedisPubSubMessageReceiver {
    void receiveMessage(final String messagePayload);
}
