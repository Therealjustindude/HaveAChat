package com.jdavies.haveachat_java_backend.module.auth.controller;

import com.jdavies.haveachat_java_backend.module.auth.dto.OAuthLoginRequest;
import com.jdavies.haveachat_java_backend.module.auth.model.TokenPair;
import com.jdavies.haveachat_java_backend.module.auth.oauth.OAuthService;
import com.jdavies.haveachat_java_backend.module.auth.oauth.OAuthServiceFactory;
import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import com.jdavies.haveachat_java_backend.module.common.oauth.AuthService;
import com.jdavies.haveachat_java_backend.module.common.oauth.OAuthUserInfo;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
@RequestMapping("/auth/oauth")
public class AuthController {

    private final OAuthServiceFactory serviceFactory;
    private final AuthService authService;

    public AuthController(OAuthServiceFactory serviceFactory, AuthService authService) {
        this.serviceFactory = serviceFactory;
        this.authService = authService;
    }

    private boolean isAlreadyAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    @PostMapping("/{provider}")
    public ResponseEntity<Void> oauthLogin(
            @PathVariable("provider") String provider,
            @RequestBody OAuthLoginRequest request,
            HttpServletResponse response,
            HttpServletRequest servletRequest
    ) {
        if (isAlreadyAuthenticated()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        AuthProvider authProvider;
        try {
            authProvider = AuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorType.BAD_REQUEST, "Invalid auth provider");
        }

        OAuthService oauthService = serviceFactory.getService(authProvider);
        OAuthUserInfo userInfo = oauthService.getUserInfo(request.getAccessToken());
        TokenPair tokenPair = authService.handleOAuthLoginWithRefresh(userInfo);

        boolean isDev = servletRequest.getServerName().equals("localhost");

        // 🔐 Create the cookies
        Cookie accessCookie = new Cookie("access_token", tokenPair.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/"); // send with every request
        accessCookie.setMaxAge(15 * 60); // 15 mins
        accessCookie.setSecure(!isDev);
        accessCookie.setAttribute("SameSite", isDev ? "Lax" : "None");

        Cookie refreshCookie = new Cookie("refresh_token", tokenPair.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/auth/oauth/refresh-token"); // Limit exposure
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        refreshCookie.setSecure(!isDev);
        refreshCookie.setAttribute("SameSite", isDev ? "Lax" : "None");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);


        if (isDev) {
            System.out.println("🔐 Access Token: " + tokenPair.getAccessToken());
            System.out.println("🔁 Refresh Token: " + tokenPair.getRefreshToken());
        }

        // You can return a 200 OK without a body
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "No cookies found");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorType.UNAUTHORIZED, "Missing refresh token"));

        if (refreshToken == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "Missing refresh token");
        }

        TokenPair newTokens = authService.refreshAccessToken(refreshToken);

        boolean isDev = request.getServerName().equals("localhost");

        Cookie accessCookie = new Cookie("access_token", newTokens.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);
        accessCookie.setSecure(!isDev);
        accessCookie.setAttribute("SameSite", isDev ? "Lax" : "None");

        response.addCookie(accessCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        boolean isDev = request.getServerName().equals("localhost");

        // Clear access token cookie
        Cookie accessCookie = new Cookie("access_token", "");
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        accessCookie.setSecure(!isDev);
        accessCookie.setAttribute("SameSite", isDev ? "Lax" : "None");

        // Clear refresh token cookie
        Cookie refreshCookie = new Cookie("refresh_token", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/auth/oauth/refresh-token");
        refreshCookie.setMaxAge(0);
        accessCookie.setSecure(!isDev);
        accessCookie.setAttribute("SameSite", isDev ? "Lax" : "None");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        // Clear auth context
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }
}
