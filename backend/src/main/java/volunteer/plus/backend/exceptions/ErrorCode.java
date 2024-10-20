package volunteer.plus.backend.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ADD_REQUEST_NOT_FOUND("Add request not found"),
    EMPTY_BRIGADE_DATA("Empty brigade data for create/update"),
    NOT_VALID_REGIMENT_CODE("Not valid regiment code"),
    BRIGADE_NOT_FOUND("Brigade not found");
    private final String data;
}
