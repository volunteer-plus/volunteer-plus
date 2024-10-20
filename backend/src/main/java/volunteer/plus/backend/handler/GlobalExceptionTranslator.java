package volunteer.plus.backend.handler;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import volunteer.plus.backend.exceptions.ApiException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionTranslator {

    public static final String SOMETHING_WENT_WRONG = "Something went wrong!";

    @ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO apiException(ApiException apiException) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST, apiException.getMessage());
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDTO internalServerError(Exception ignoredEx) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, SOMETHING_WENT_WRONG);

    }

    @Getter
    @Setter
    public record ErrorDTO(HttpStatus httpStatus, String msg) {
    }
}
