package volunteer.plus.backend.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import volunteer.plus.backend.exceptions.ApiException;

@RestControllerAdvice
public class GlobalExceptionTranslator {

    @ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String apiException(ApiException apiException) {
        return apiException.getMessage();
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String internalServerError(Exception ex) {
        return ex.getMessage();
    }

}
