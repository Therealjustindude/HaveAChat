package com.jdavies.haveachat_java_backend.module.chat.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_id"}))
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;


    @Column(nullable = false, updatable = false)
    private final Instant createdAt = Instant.now();

    @Column(nullable = true)
    private LocalDateTime modifiedAt;

    @Column(nullable = true)
    private LocalDateTime readAt;

    @Column(nullable = false)
    private Boolean isDelivered = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = true)
    private Integer totalReplies;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode message;

    protected ChatMessage() {
    }

    public ChatMessage(Long userId, Long channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public JsonNode getMessage() {
        return message;
    }

    public void setMessage(JsonNode message) {
        this.message = message;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getTotalReplies() {
        return totalReplies;
    }

    public void setTotalReplies(Integer totalReplies) {
        this.totalReplies = totalReplies;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
