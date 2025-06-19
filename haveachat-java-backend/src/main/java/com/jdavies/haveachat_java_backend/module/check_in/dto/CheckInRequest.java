package com.jdavies.haveachat_java_backend.module.check_in.dto;

import java.time.Instant;

public class CheckInRequest {
    private Long golfCourseId;
    private String intent;
    private String message;
    private boolean openToChat;
    private Instant expiresAt;

    // Getters and Setters
    public Long getGolfCourseId() { return golfCourseId; }
    public void setGolfCourseId(Long golfCourseId) { this.golfCourseId = golfCourseId; }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isOpenToChat() { return openToChat; }
    public void setOpenToChat(boolean openToChat) { this.openToChat = openToChat; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
