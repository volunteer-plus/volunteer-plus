package volunteer.plus.backend.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AbstractMessage;
import volunteer.plus.backend.domain.mixin.ai.AIMessageMixin;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;

import java.io.IOException;

public class JacksonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    public static final ObjectMapper polymorphicObjectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .addMixIn(AbstractMessage.class, AIMessageMixin.class);


    private JacksonUtil() {
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        } else {
            try {
                return polymorphicObjectMapper.readValue(json, clazz);
            } catch (IOException e) {
                throw new ApiException(ErrorCode.CANNOT_DESERIALIZE_JSON);
            }
        }
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

    public static String serialize(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (IOException e) {
                throw new ApiException(ErrorCode.CANNOT_SERIALIZE_JSON);
            }
        }
    }
}
