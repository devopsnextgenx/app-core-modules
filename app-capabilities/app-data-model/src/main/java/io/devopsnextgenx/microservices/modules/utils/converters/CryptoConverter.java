package io.devopsnextgenx.microservices.modules.utils.converters;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PBEStringEncryptor;

import io.devopsnextgenx.microservices.modules.utils.context.PersistenceEncryptionContext;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CryptoConverter:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @since May 4, 2019
 */
@Slf4j
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String message) {
        return getEncryptor().encrypt(message);
    }

    @Override
    public String convertToEntityAttribute(String encryptedMessage) {
        try {
            return getEncryptor().decrypt(encryptedMessage);
        } catch (Exception e) {

        }
        return encryptedMessage;
    }
    private PBEStringEncryptor getEncryptor() {
        log.info("CryptoConverter: Encryptor Key Used: [{}]", PersistenceEncryptionContext.getInstance().getPassword());
        return PersistenceEncryptionContext.getInstance().getEncryptor();
    }
}