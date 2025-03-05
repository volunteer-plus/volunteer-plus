package volunteer.plus.backend.exceptions;

public class ApiException extends RuntimeException {
    public ApiException(ErrorCode errorCode) {
        super(errorCode.getData());
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Exception e) {
        super(e);
    }
}
