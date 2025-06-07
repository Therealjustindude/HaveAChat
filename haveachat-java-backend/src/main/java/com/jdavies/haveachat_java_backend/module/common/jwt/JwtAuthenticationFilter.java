package com.jdavies.haveachat_java_backend.module.common.jwt;

import com.jdavies.haveachat_java_backend.module.auth.model.RefreshToken;
import com.jdavies.haveachat_java_backend.module.auth.service.RefreshTokenService;
import com.jdavies.haveachat_java_backend.module.auth.util.JwtUtil;
import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip filtering for swagger-related endpoints
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractTokenFromCookie(request, "access_token");

        if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
            // 🟢 Token is valid, proceed
            String oauthId = jwtUtil.extractOauthId(accessToken);
            AuthProvider provider = jwtUtil.extractProvider(accessToken);
            setAuthContext(oauthId, provider);
        } else {
            // 🔴 Access token missing/expired - try refresh
            String refreshToken = extractTokenFromCookie(request, "refresh_token");

            if (StringUtils.hasText(refreshToken) && refreshTokenService.isTokenValid(refreshToken)) {
                RefreshToken token = refreshTokenService.getToken(refreshToken);
                AuthProvider provider = token.getProvider();
                String oauthId = token.getOauthId();
                String email = token.getEmail();

                String newAccessToken = jwtUtil.generateToken(provider, oauthId, email);

                // 🔄 Set new access token cookie
                Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
                newAccessCookie.setHttpOnly(true);
                newAccessCookie.setSecure(true);
                newAccessCookie.setPath("/");
                newAccessCookie.setMaxAge(15 * 60);
                newAccessCookie.setAttribute("SameSite", "None");
                response.addCookie(newAccessCookie);

                setAuthContext(oauthId, provider);
            } else {
                // 👎 No valid token, deny
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/login",
            "/auth/oauth/google",
            "/auth/refresh"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.contains(path);
    }

    private void setAuthContext(String oauthId, AuthProvider provider) {
        // Load UserDetails from DB if needed and set context
        userService.findByOauthIdAndProvider(oauthId, provider).ifPresent(userDetails -> {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        });
    }

    private String extractTokenFromCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
