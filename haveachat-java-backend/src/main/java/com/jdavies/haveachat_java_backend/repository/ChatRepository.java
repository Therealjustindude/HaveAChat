package com.jdavies.haveachat_java_backend.repository;

import com.jdavies.haveachat_java_backend.model.Channel;
import com.jdavies.haveachat_java_backend.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Channel, Long> {
    List<Chat> findByChannelId(Long channelId);

    // All memberships for a given user
    List<Chat> findByUserId(Long userId);

    List<Chat> findByChannelIdAndCreatedAtAfter(Long channelId, Instant since);
}
