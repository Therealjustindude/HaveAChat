package com.jdavies.haveachat_java_backend.module.auth.dto;

public class OAuthLoginRequest {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
