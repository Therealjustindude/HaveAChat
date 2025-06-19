package com.jdavies.haveachat_java_backend.module.check_in.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;
@Entity
@Table(name = "check_ins")
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long golfCourseId;

    private String intent;

    private String message;

    private boolean openToChat;

    @Column(nullable = false)
    private Instant checkInTime = Instant.now();

    private Instant expiresAt;

    public Instant getEffectiveExpiration() {
        return (expiresAt != null) ? expiresAt : checkInTime.plus(Duration.ofHours(4));
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGolfCourseId() { return golfCourseId; }

    public void setGolfCourseId(Long golfCourseId) { this.golfCourseId = golfCourseId; }

    public String getIntent() { return intent; }

    public void setIntent(String intent) { this.intent = intent; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public boolean isOpenToChat() { return openToChat; }

    public void setOpenToChat(boolean openToChat) { this.openToChat = openToChat; }

    public Instant getCheckInTime() { return checkInTime; }

    public void setCheckInTime(Instant checkInTime) { this.checkInTime = checkInTime; }

    public Instant getExpiresAt() { return expiresAt; }

    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
