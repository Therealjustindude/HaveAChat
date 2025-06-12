package com.jdavies.haveachat_java_backend.module.chat.repository;

import com.jdavies.haveachat_java_backend.module.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChannelId(Long channelId);

    // All memberships for a given user
    List<ChatMessage> findByUserId(Long userId);

    List<ChatMessage> findByChannelIdAndCreatedAtAfter(Long channelId, Instant since);
    List<ChatMessage> findByChannelIdAndReadAtIsNull(Long channelId);
}
