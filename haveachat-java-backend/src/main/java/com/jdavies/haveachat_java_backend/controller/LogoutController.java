package com.jdavies.haveachat_java_backend.controller;

import com.jdavies.haveachat_java_backend.config.OAuthSecurityConfig;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {
    private static final Logger logger = LoggerFactory.getLogger(OAuthSecurityConfig.class);

    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Logout endpoint hit");

        if (auth != null) {
            logger.info("User '{}' is logging out. Authorities: {}", auth.getName(), auth.getAuthorities());
            new SecurityContextLogoutHandler().logout(request, response, auth);
            logger.info("Logout handler executed");
        } else {
            logger.info("No authentication found in security context");
        }

        // Explicitly delete cookies by setting Max-Age to 0
        deleteCookie("JSESSIONID", request, response);
        deleteCookie("XSRF-TOKEN", request, response);


        logger.info("Setting HTTP response status to 200 OK");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void deleteCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(false); // Set to true if the cookie is HttpOnly
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
    }
}