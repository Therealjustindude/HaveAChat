package com.jdavies.haveachat_java_backend.module.auth.oauth;

import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import com.jdavies.haveachat_java_backend.module.common.oauth.OAuthUserInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleOAuthService implements OAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> body = response.getBody();
            if (body.get("sub") == null) {
                throw new RuntimeException("Invalid token: user info not found");
            }

            return new OAuthUserInfo(
                    (String) body.get("sub"),
                    (String) body.get("email"),
                    (String) body.get("name"),
                    AuthProvider.GOOGLE
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Google user info", e);
        }
    }
}
