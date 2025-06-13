package com.jdavies.haveachat_java_backend.module.auth.util;

import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(AuthProvider provider, String oauthId, String email) {
        return Jwts.builder()
                .setSubject(provider + "|" + oauthId) // e.g., "google|abc123"
                .claim("email", email)
                .claim("provider", provider)
                .claim("oauthId", oauthId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract full identifier like "google|abc123"
    public String extractProviderSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public AuthProvider extractProvider(String token) {
        String providerStr = extractAllClaims(token).get("provider", String.class);
        return AuthProvider.valueOf(providerStr);
    }

    public String extractOauthId(String token) {
        return extractAllClaims(token).get("oauthId", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
