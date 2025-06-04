package com.jdavies.haveachat_java_backend.module.auth.config;

import com.jdavies.haveachat_java_backend.module.auth.security.JwtAuthenticationFilter;
import com.jdavies.haveachat_java_backend.module.auth.util.JwtUtil;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring HTTP Security...");

        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/auth/google/login",
                                "/api/logout"
                        ).permitAll()  // Allow these URLs
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            String googleId = oauth2User.getAttribute("sub");
                            String email = oauth2User.getAttribute("email");
                            String name = oauth2User.getAttribute("name");

                            userService.createOrUpdateUserOAuth(googleId, name, email);

                            // Generate JWT
                            String token = jwtUtil.generateToken(googleId, email);

                            // Send token in response (e.g., as JSON or as a redirect with token as query param)
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"token\":\"" + token + "\"}");
                        })
                        .failureHandler((request, response, exception) -> {
                            // Handle authentication failure
                            response.sendRedirect("http://localhost:3000/login?error");
                        })
                )
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class);

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