package com.jdavies.haveachat_java_backend.module.chat.model;

public class TypingNotification {
    private Long userId;
    private Long channelId;
    private boolean isTyping;

    public TypingNotification() {}

    public TypingNotification(Long userId, Long channelId, boolean isTyping) {
        this.userId = userId;
        this.channelId = channelId;
        this.isTyping = isTyping;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setIsTyping(boolean isTyping) {
        this.isTyping = isTyping;
    }
}
