package volunteer.plus.backend.config.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Converter
public class EmailAttributeConverter implements AttributeConverter<String, String> {

    private static StringEncryptor encryptor;

    @Autowired
    public void setEncryptor(@Qualifier("deterministicEncryptor") StringEncryptor enc) {
        EmailAttributeConverter.encryptor = enc;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return encryptor.encrypt(attribute.trim().toLowerCase());
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return encryptor.decrypt(dbData);
    }
}
