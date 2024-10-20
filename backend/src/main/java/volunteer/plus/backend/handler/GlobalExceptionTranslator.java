package volunteer.plus.backend.handler;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import volunteer.plus.backend.exceptions.ApiException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionTranslator {

    @ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO apiException(ApiException apiException) {
        log.error("API exception occurred: {}", apiException.getMessage(), apiException);
        return new ErrorDTO(HttpStatus.BAD_REQUEST, apiException.getMessage());
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
