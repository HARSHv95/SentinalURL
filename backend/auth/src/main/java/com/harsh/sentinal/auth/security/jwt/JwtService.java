package com.harsh.sentinal.auth.security.jwt;

import com.harsh.sentinal.auth.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String sercret_key;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(sercret_key)
        );
    }

    public String generateToken(String username, String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);

        return Jwts.builder()

                .claims(claims)

                .subject(username)

                .issuedAt(now)

                .expiration(expiry)

                .signWith(getSigningKey(), SignatureAlgorithm.HS256)

                .compact();

    }
}
