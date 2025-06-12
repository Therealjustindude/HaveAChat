package com.jdavies.haveachat_java_backend.module.channel.model;

import com.jdavies.haveachat_java_backend.module.user.model.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "channel_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_id"})
)
public class ChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @Column(nullable = false, updatable = false)
    private final Instant createdAt = Instant.now();

    protected ChannelMember() {
    }

    public ChannelMember(Long userId, Long channelId) {
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

    public Instant getCreatedAt() {
        return createdAt;
    }
}
