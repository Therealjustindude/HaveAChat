package com.jdavies.haveachat_java_backend.config;

import com.jdavies.haveachat_java_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/google/login", "/logout").permitAll()  // Allow these URLs
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

                    // Redirect to home or another page after successful login
                    logger.info("Redirecting to localhost 3000 / after login.");

                    // Redirect to home or another page after successful login
                    response.sendRedirect("http://localhost:3000/");
                })
                .failureHandler((request, response, exception) -> {
                    // Handle authentication failure
                    response.sendRedirect("http://localhost:3000/login?error");
                })
            );

        return http.build();  // Return the configured HttpSecurity object
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);  // Allow credentials (cookies, HTTP authentication, etc.)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}