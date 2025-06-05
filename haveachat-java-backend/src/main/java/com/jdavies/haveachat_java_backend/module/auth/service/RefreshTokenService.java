package com.jdavies.haveachat_java_backend.module.auth.service;

import com.jdavies.haveachat_java_backend.module.auth.model.RefreshToken;
import com.jdavies.haveachat_java_backend.module.auth.repository.RefreshTokenRepository;
import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration.ms}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.refreshTokenRepo = repo;
    }

    public String createRefreshToken(AuthProvider provider, String oauthId, String email) {
        RefreshToken token = new RefreshToken();

        String rawToken = UUID.randomUUID().toString(); // This is what the client gets
        String hashedToken = hashToken(rawToken);       // This is what we store

        token.setToken(hashedToken);
        token.setOauthId(oauthId);
        token.setProvider(provider);
        token.setEmail(email);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setRevoked(false);

        refreshTokenRepo.save(token);

        return rawToken;
    }

    public boolean isTokenValid(String rawToken) {
        String hashed = hashToken(rawToken);

        return refreshTokenRepo.findByToken(hashed)
                .filter(t -> !t.isRevoked() && t.getExpiryDate().isAfter(Instant.now()))
                .isPresent();
    }

    public RefreshToken getToken(String rawToken) {
        String hashed = hashToken(rawToken);
        RefreshToken token = refreshTokenRepo.findByToken(hashed)
                .orElseThrow(() -> new CustomException(ErrorType.UNAUTHORIZED, "Refresh token not found"));

        if (token.isRevoked()) {
            // 🚨 Token reuse attempt — log this for security analysis
            throw new CustomException(ErrorType.UNAUTHORIZED, "Refresh token has been revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "Refresh token has expired");
        }

        return token;
    }

    public void revokeToken(String rawToken) {
        String hashed = hashToken(rawToken);

        refreshTokenRepo.findByToken(hashed).ifPresent(t -> {
            t.setRevoked(true);
            refreshTokenRepo.save(t);
        });
    }

    public void revokeAllTokensForUser(String oauthId) {
        refreshTokenRepo.deleteByOauthId(oauthId);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing refresh token", e);
        }
    }
}
