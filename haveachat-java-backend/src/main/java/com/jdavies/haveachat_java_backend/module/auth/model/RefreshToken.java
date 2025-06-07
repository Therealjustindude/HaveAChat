package com.jdavies.haveachat_java_backend.module.auth.model;

import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private  String oauthId;

    private AuthProvider provider;

    private String email;

    private Instant expiryDate;

    private boolean revoked;

    // Default constructor
    public RefreshToken() {
    }

    // Full constructor
    public RefreshToken(Long id, String token, String oauthId, Instant expiryDate, boolean revoked, AuthProvider provider, String email) {
        this.id = id;
        this.token = token;
        this.oauthId = oauthId;
        this.provider= provider;
        this.email = email;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
