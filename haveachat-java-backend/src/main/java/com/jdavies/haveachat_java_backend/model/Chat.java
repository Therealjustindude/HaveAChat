package com.jdavies.haveachat_java_backend.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

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

    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode message;

    protected Chat() {
    }

    public Chat(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
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

    public Boolean getIsDelivered() { return isDelivered; }
    public void setIsDelivered(Boolean isDelivered) { this.isDelivered = isDelivered; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Integer getTotalReplies() { return totalReplies; }
    public void setTotalReplies(Integer totalReplies) {
        this.totalReplies = totalReplies;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
