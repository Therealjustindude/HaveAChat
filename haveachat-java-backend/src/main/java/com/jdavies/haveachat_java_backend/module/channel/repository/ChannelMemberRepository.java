package com.jdavies.haveachat_java_backend.module.channel.repository;

import com.jdavies.haveachat_java_backend.module.channel.model.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    // All memberships in a given channel
    List<ChannelMember> findByChannelId(Long channelId);

    // All memberships for a given user
    List<ChannelMember> findByUserId(Long userId);

    // Check if a user is already in a channel
    boolean existsByUserIdAndChannelId(Long userId, Long channelId);

    // Delete a membership by user & channel
    void deleteByUserIdAndChannelId(Long userId, Long channelId);
}
