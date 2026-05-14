package com.example.cookieBank.component;

import com.example.cookieBank.repository.entities.RoleClients;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secretString = "mySuperSecretKeyForCookieBankProject2026!!!";
    private final SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    private final Long jwtExpirationsMs = 3600000L;

    public String generateToken(Long clientId, String role) {
        return Jwts.builder()
                .subject(String.valueOf(clientId))
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationsMs))
                .signWith(key)
                .compact();
    }

    public boolean validatedToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getClientIdFromToken(String token) {
        String id = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        return Long.parseLong(id);
    }

    public String getClientRoleFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
}
