package com.harsh.sentinal.scan.security.discord;

import com.harsh.sentinal.scan.integration.discord.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.NamedParameterSpec;
import java.security.spec.EdECPoint;
import java.security.spec.EdECPublicKeySpec;
import java.util.HexFormat;

/**
 * Verifies Discord's Ed25519 interaction signatures, natively supported since
 * Java 15 (java.security.Signature / java.security.spec.EdECPublicKeySpec) —
 * no BouncyCastle or other crypto dependency needed. Discord signs
 * (timestamp + rawBody); the signature and public key are both hex-encoded.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordSignatureVerifier {

    private final DiscordProperties properties;

    public boolean verify(String signatureHex, String timestamp, String rawBody) {
        if (signatureHex == null || timestamp == null || rawBody == null) {
            return false;
        }

        try {
            byte[] signature = HexFormat.of().parseHex(signatureHex);
            byte[] message = (timestamp + rawBody).getBytes(StandardCharsets.UTF_8);
            PublicKey publicKey = buildPublicKey(properties.getPublicKey());

            Signature verifier = Signature.getInstance("Ed25519");
            verifier.initVerify(publicKey);
            verifier.update(message);

            return verifier.verify(signature);
        } catch (Exception e) {
            log.warn("Discord signature verification failed: {}", e.getMessage());
            return false;
        }
    }

    private PublicKey buildPublicKey(String publicKeyHex) throws Exception {
        byte[] rawKey = HexFormat.of().parseHex(publicKeyHex);

        // Ed25519 public keys are 32 bytes; the high bit of the last byte is the
        // sign of the X coordinate, per RFC 8032, and the rest (little-endian) is Y.
        byte[] yBytes = rawKey.clone();
        boolean xOdd = (yBytes[31] & 0x80) != 0;
        yBytes[31] &= 0x7F;

        byte[] yBigEndian = new byte[32];
        for (int i = 0; i < 32; i++) {
            yBigEndian[i] = yBytes[31 - i];
        }

        java.math.BigInteger y = new java.math.BigInteger(1, yBigEndian);
        EdECPoint point = new EdECPoint(xOdd, y);
        EdECPublicKeySpec keySpec = new EdECPublicKeySpec(NamedParameterSpec.ED25519, point);

        KeyFactory keyFactory = KeyFactory.getInstance("Ed25519");
        return keyFactory.generatePublic(keySpec);
    }
}
