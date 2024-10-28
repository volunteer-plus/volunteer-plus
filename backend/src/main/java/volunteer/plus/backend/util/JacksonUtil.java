package volunteer.plus.backend.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;

import java.io.IOException;

public class JacksonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    private JacksonUtil() {
    }

    public static <T> T deserialize(String json, TypeReference<T> type) {
        if (json == null) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, type);
            } catch (IOException e) {
                throw new ApiException(ErrorCode.CANNOT_DESERIALIZE_JSON);
            }
        }
    }
}
