package com.harsh.sentinal.scan.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class TokenEncryptionService {

    private final TextEncryptor encryptor;

    public TokenEncryptionService(
            @Value("${gmail.token-encryption-key}") String key,
            @Value("${gmail.token-encryption-salt}") String salt) {
        this.encryptor = Encryptors.delux(key, salt);
    }

    public String encrypt(String plaintext) {
        return encryptor.encrypt(plaintext);
    }

    public String decrypt(String ciphertext) {
        return encryptor.decrypt(ciphertext);
    }
}
