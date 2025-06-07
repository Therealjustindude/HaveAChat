package com.jdavies.haveachat_java_backend.module.auth.repository;

import com.jdavies.haveachat_java_backend.module.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByOauthId(String oauthId);
}
