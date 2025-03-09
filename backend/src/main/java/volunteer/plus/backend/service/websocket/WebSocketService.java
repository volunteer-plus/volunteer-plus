package volunteer.plus.backend.service.websocket;

public interface WebSocketService {
    void sendNotification(String destination, Object payload);
}
