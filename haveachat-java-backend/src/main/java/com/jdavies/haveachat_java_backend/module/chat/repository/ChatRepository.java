package com.jdavies.haveachat_java_backend.module.chat.repository;

import com.jdavies.haveachat_java_backend.module.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChannelId(Long channelId);

    // All memberships for a given user
    List<Chat> findByUserId(Long userId);

    List<Chat> findByChannelIdAndCreatedAtAfter(Long channelId, Instant since);
}
