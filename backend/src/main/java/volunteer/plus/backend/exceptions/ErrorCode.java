package volunteer.plus.backend.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NO_ACTIVE_DATABASE("No active database");
    private final String data;
}
