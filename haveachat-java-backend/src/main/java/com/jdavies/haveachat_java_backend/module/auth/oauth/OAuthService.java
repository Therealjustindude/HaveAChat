package com.jdavies.haveachat_java_backend.module.auth.oauth;

import com.jdavies.haveachat_java_backend.module.common.oauth.OAuthUserInfo;

public interface OAuthService {
    OAuthUserInfo getUserInfo(String accessToken); // throws if invalid
}
