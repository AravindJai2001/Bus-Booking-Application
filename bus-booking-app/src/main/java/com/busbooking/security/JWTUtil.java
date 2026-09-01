package com.busbooking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;

@Component
public class JWTUtil {

    private final String SECRET_KEY = "LGQDdrbnXhTud9xEYVbI2kimsWQf9UUFtS+cTMGiPlM=";
    private final byte[] secretKeyBytes = Decoders.BASE64.decode(SECRET_KEY);
    private final SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);

    public String generateToken(String email, Long userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token valid for 1 Hour
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }

    public Long extractUserId(String token){
        return getClaims(token).get("userId", Long.class);
    }

    public boolean isTokenValid(String token, String emailId){
        return extractEmail(token).equals(emailId) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
