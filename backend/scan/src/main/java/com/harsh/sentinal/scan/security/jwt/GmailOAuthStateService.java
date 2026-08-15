package com.harsh.sentinal.scan.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Signs/verifies a short-lived state token identifying which user is
 * completing the Gmail OAuth redirect. Separate from JwtService (which is
 * verify-only and never issues tokens) rather than growing its contract.
 */
@Service
public class GmailOAuthStateService {

    private static final Duration STATE_TTL = Duration.ofMinutes(5);

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateState(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(STATE_TTL)))
                .signWith(signingKey())
                .compact();
    }

    public UUID resolveUserId(String state) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(state)
                .getPayload();

        return UUID.fromString(claims.getSubject());
    }
}
