package com.jdavies.haveachat_java_backend.module.channel.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false, updatable = false)
    private final Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private boolean privateChannel = false;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setPrivateChannel(boolean privateChannel) {
        this.privateChannel = privateChannel;
    }

    public boolean getPrivateChannel() {
        return privateChannel;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }
}
