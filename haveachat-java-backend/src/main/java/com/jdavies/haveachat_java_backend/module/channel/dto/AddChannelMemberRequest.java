package com.jdavies.haveachat_java_backend.module.channel.dto;

import jakarta.validation.constraints.NotNull;

public class AddChannelMemberRequest {
    @NotNull(message = "userId must not be null")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
