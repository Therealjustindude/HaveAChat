package com.jdavies.haveachat_java_backend.config;

import com.jdavies.haveachat_java_backend.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import java.util.List;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(OAuthSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring HTTP Security...");

        http
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/google/login", "/api/logout").permitAll()  // Allow these URLs
                .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                    String googleId = oauth2User.getAttribute("sub");  // Get Google user ID
                    String name = oauth2User.getAttribute("name");  // Get user's name
                    String email = oauth2User.getAttribute("email");  // Get user's email
                    
                    logger.info("OAuth2 login successful. Google ID: {}, Name: {}, Email: {}", googleId, name, email);

                    // Save or update the user in the DB using UserService
                    userService.createOrUpdateUser(googleId, name, email);

                    // Ensure CSRF token is set during the login
                    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    if (csrfToken != null) {
                        // Set the CSRF token in both response header and cookie
                        logger.info("CSRF Token after login: {}", csrfToken.getToken());
                        response.setHeader("X-XSRF-TOKEN", csrfToken.getToken());  // This is for frontend to pick up
                        Cookie cookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
                        cookie.setPath("/");  // Ensure the cookie is available for all paths
                        cookie.setHttpOnly(false);  // Allow JS to access it
                        cookie.setMaxAge(24 * 60 * 60);  // 1 day expiration (optional)
                        response.addCookie(cookie);
                    }

                    // Redirect to home or another page after successful login
                    logger.info("Redirecting to localhost 3000 / after login.");

                    // Redirect to home or another page after successful login
                    response.sendRedirect("http://localhost:3000/");
                })
                .failureHandler((request, response, exception) -> {
                    // Handle authentication failure
                    response.sendRedirect("http://localhost:3000/login?error");
                })
            )
            .logout(AbstractHttpConfigurer::disable);

        return http.build();  // Return the configured HttpSecurity object
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}