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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(nullable = false, updatable = false)
    private final Instant createdAt = Instant.now();

    protected ChannelMember() {
    }

    public ChannelMember(User user, Channel channel) {
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

    public Instant getCreatedAt() {
        return createdAt;
    }
}
