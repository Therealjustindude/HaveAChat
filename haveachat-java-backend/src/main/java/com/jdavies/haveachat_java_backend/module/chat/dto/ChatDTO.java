package com.jdavies.haveachat_java_backend.module.chat.dto;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.LocalDateTime;

public class ChatDTO {
    private Long      id;           // for React keying / edits
    private Long      channelId;    // so you know what room this belongs to
    private Long      userId;       // who sent it
    private String    userName;     // (optional) for display next to the message
    private Instant   createdAt;    // timestamp to show “sent at…”
    private LocalDateTime modifiedAt;   // if you support editing
    private JsonNode message;

    public ChatDTO() {}

    public Long getId() {
        return id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public JsonNode getMessage() {
        return message;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setMessage(JsonNode message) {
        this.message = message;
    }
}
