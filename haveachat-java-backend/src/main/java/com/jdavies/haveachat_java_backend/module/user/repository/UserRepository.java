package com.jdavies.haveachat_java_backend.module.user.repository;

import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthIdAndProvider(String oauthId, AuthProvider provider);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByLastLoginAfter(LocalDateTime lastLogin);

    List<User> findByLastLoginBefore(LocalDateTime lastLogin);

    List<User> findByCreatedAt(LocalDateTime createdAt);
}