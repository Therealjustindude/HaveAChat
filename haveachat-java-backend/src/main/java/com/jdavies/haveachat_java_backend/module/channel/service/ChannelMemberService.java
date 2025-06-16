package com.jdavies.haveachat_java_backend.module.channel.service;

import com.jdavies.haveachat_java_backend.module.channel.model.ChannelMember;
import com.jdavies.haveachat_java_backend.module.channel.repository.ChannelMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelMemberService {

    private final ChannelMemberRepository channelMemberRepository;

    @Autowired
    public ChannelMemberService(ChannelMemberRepository channelMemberRepository) {
        this.channelMemberRepository = channelMemberRepository;
    }

    public List<ChannelMember> getMembersByChannelId(Long channelId) {
        return channelMemberRepository.findByChannelId(channelId);
    }

    public List<ChannelMember> getMembershipsByUserId(Long userId) {
        return channelMemberRepository.findByUserId(userId);
    }

    public List<Long> getChannelIdsForUser(Long userId) {
        List<ChannelMember> memberships = channelMemberRepository.findByUserId(userId);
        return memberships.stream()
                .map(ChannelMember::getChannelId)
                .distinct()
                .toList();
    }

    public boolean isUserMemberOfChannel(Long userId, Long channelId) {
        return channelMemberRepository.existsByUserIdAndChannelId(userId, channelId);
    }

    public void addMember(ChannelMember channelMember) {
        channelMemberRepository.save(channelMember);
    }

    @Transactional
    public void removeMember(Long userId, Long channelId) {
        channelMemberRepository.deleteByUserIdAndChannelId(userId, channelId);
    }
}
