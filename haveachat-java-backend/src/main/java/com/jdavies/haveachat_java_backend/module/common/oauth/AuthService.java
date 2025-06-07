package com.jdavies.haveachat_java_backend.module.common.oauth;

import com.jdavies.haveachat_java_backend.module.auth.model.RefreshToken;
import com.jdavies.haveachat_java_backend.module.auth.model.TokenPair;
import com.jdavies.haveachat_java_backend.module.auth.service.RefreshTokenService;
import com.jdavies.haveachat_java_backend.module.auth.util.JwtUtil;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserService userService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    public TokenPair handleOAuthLoginWithRefresh(OAuthUserInfo userInfo) {
        User user = userService.createOrUpdateUserOAuth(userInfo);
        String accessToken = jwtUtil.generateToken(
                userInfo.getProvider(),
                userInfo.getOauthId(),
                userInfo.getEmail()
        );
        String refreshToken = refreshTokenService.createRefreshToken(user.getProvider(), user.getOauthId(), user.getEmail());

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair refreshAccessToken(String refreshTokenVal) {
        RefreshToken refreshToken = refreshTokenService.getToken(refreshTokenVal);
        // Generate new tokens
        String newAccessToken = jwtUtil.generateToken(
                refreshToken.getProvider(),
                refreshToken.getOauthId(),
                refreshToken.getEmail()
        );
        String newRefreshToken = refreshTokenService.createRefreshToken(
                refreshToken.getProvider(),
                refreshToken.getOauthId(),
                refreshToken.getEmail()
        );
        // 👇 Invalidate the old one so it can't be used again
        refreshTokenService.revokeToken(refreshToken.getToken());

        return new TokenPair(newAccessToken, newRefreshToken);
    }
}
