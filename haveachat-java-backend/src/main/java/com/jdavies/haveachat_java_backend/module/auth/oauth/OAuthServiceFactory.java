package com.jdavies.haveachat_java_backend.module.auth.oauth;

import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class OAuthServiceFactory {

    private final Map<AuthProvider, OAuthService> services = new EnumMap<>(AuthProvider.class);

    @Autowired
    public OAuthServiceFactory(GoogleOAuthService googleOAuthService) {
        services.put(AuthProvider.GOOGLE, googleOAuthService);
        // Add more as needed
    }

    public OAuthService getService(AuthProvider provider) {
        return services.get(provider);
    }
}
