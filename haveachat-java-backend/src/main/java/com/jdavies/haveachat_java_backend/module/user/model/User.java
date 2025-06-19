package com.jdavies.haveachat_java_backend.module.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonIgnore
    private String oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    @JsonIgnore
    private String password;

    @Column(nullable = false, updatable = false)
    private final Instant createdAt = Instant.now();

    @Column(nullable = true)
    private LocalDateTime lastLogin;

    @Column(nullable = true)
    private Double latitude;
    @Column(nullable = true)
    private Double longitude;
    @Column(nullable = true)
    private LocalDateTime locationUpdatedAt;

    public Long getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getLocationUpdatedAt() {
        return locationUpdatedAt;
    }

    public void setLocationUpdatedAt(LocalDateTime locationUpdatedAt) {
        this.locationUpdatedAt = locationUpdatedAt;
    }
}