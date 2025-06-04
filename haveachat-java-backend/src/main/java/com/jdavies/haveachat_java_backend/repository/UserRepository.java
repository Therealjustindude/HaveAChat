package com.jdavies.haveachat_java_backend.repository;

import com.jdavies.haveachat_java_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByLastLoginAfter(LocalDateTime lastLogin);

    List<User> findByLastLoginBefore(LocalDateTime lastLogin);

    List<User> findByCreatedAt(LocalDateTime createdAt);

//	@Modifying
//	@Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :id")
//	@Transactional
//	void updateLastLogin(@Param("id") Long id, @Param("lastLogin") LocalDateTime lastLogin);
}