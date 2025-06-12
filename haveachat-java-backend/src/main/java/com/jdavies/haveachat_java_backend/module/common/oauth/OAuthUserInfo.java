package com.jdavies.haveachat_java_backend.module.common.oauth;

public class OAuthUserInfo {
    private final String oauthId;
    private final String email;
    private final String name;
    private final AuthProvider provider;

    public OAuthUserInfo(String oauthId, String email, String name, AuthProvider provider) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.provider = provider;
    }

    public String getOauthId() { return oauthId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public AuthProvider getProvider() { return provider; }
}
