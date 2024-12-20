package io.devopsnextgenx.microservices.modules.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Converter
@Component
public class PasswordEncryptor implements AttributeConverter<String, String> {

    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return jasyptStringEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return jasyptStringEncryptor.decrypt(dbData);
    }
}