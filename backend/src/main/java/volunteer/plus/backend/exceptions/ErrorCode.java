package volunteer.plus.backend.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ADD_REQUEST_NOT_FOUND("Add request not found"),
    EMPTY_BRIGADE_DATA("Empty brigade data for create/update"),
    EMPTY_MILITARY_PERSONNEL_DATA("Empty military personnel data for create"),
    NOT_VALID_REGIMENT_CODE("Not valid regiment code"),
    BRIGADE_NOT_FOUND("Brigade not found"),
    ADD_REQUEST_IS_ALREADY_EXECUTED("Add request is already executed"),
    MILITARY_PERSONNEL_NOT_FOUND("Military personnel not found");
    private final String data;
}
