package volunteer.plus.backend.domain.entity.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.ai.chat.messages.AbstractMessage;
import volunteer.plus.backend.util.JacksonUtil;

@Converter
public class MessageJsonConverter implements AttributeConverter<AbstractMessage, String> {

    @Override
    public String convertToDatabaseColumn(AbstractMessage message) {
        return JacksonUtil.serialize(message);
    }

    @Override
    public AbstractMessage convertToEntityAttribute(String dbData) {
        return JacksonUtil.deserialize(dbData, AbstractMessage.class);
    }
}
