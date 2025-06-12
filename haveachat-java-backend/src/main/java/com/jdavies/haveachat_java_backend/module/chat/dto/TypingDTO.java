package com.jdavies.haveachat_java_backend.module.chat.dto;

public class TypingDTO {
    private boolean      isTyping;
    private Long      channelId;
    private Long      userId;

    public boolean getIsTyping() {
        return isTyping;
    }


    public Long getChannelId() {
        return channelId;
    }

    public Long getUserId() {
        return userId;
    }

    // Setters
    public void setIsTyping(boolean isTyping) {
        this.isTyping = isTyping;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}