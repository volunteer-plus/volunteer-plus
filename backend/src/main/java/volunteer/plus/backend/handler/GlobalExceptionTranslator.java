package volunteer.plus.backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.EmailException;

import java.io.IOException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionTranslator {

    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .build();

    @ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO apiException(ApiException apiException) {
        log.error("API exception occurred: {}", apiException.getMessage(), apiException);
        return new ErrorDTO(HttpStatus.BAD_REQUEST, apiException.getMessage());
    }

    @ExceptionHandler(value = {EmailException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleEmailException(EmailException exception) {
        log.error("Email exception occurred: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Email service error: " + exception.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("Illegal argument exception occurred: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.BAD_REQUEST, "Invalid argument: " + exception.getMessage());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleNullPointerException(NullPointerException exception) {
        log.error("Null pointer exception occurred: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "A null pointer exception occurred. Please contact support.");
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleAccessDeniedException(AccessDeniedException exception) {
        log.error("Access denied: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.FORBIDDEN, "Access denied: " + exception.getMessage());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error("Entity not found: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.NOT_FOUND, "Resource not found: " + exception.getMessage());
    }

    @ExceptionHandler(value = {UnsupportedOperationException.class})
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ErrorDTO handleUnsupportedOperationException(UnsupportedOperationException exception) {
        log.error("Unsupported operation: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.NOT_IMPLEMENTED, "This operation is not supported: " + exception.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGenericException(Exception exception) {
        log.error("Unexpected exception occurred: {}", exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static final class ErrorDTO {
        private HttpStatus httpStatus;
        private String msg;
    }
}
