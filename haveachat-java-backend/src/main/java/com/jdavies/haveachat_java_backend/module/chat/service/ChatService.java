package com.jdavies.haveachat_java_backend.module.chat.service;

import com.jdavies.haveachat_java_backend.module.chat.mapper.ChatMapper;
import com.jdavies.haveachat_java_backend.module.chat.dto.ChatDTO;
import com.jdavies.haveachat_java_backend.module.chat.repository.ChatRepository;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.channel.repository.ChannelRepository;
import com.jdavies.haveachat_java_backend.module.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatRepository chatRepo;
    private final ChannelRepository channelRepo;
    private final UserRepository userRepo;

    @Autowired
    public ChatService(
            ChannelRepository channelRepo,
            UserRepository userRepo,
            ChatRepository chatRepo
    ) {
        this.chatRepo= chatRepo;
        this.channelRepo = channelRepo;
        this.userRepo = userRepo;
    }

    /**
     * Fetch message history for a channel, optionally since a given timestamp or ID.
     */
    public List<ChatDTO> getHistory(Long channelId, Instant since) {
        if (!channelRepo.existsById(channelId)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Channel not found: " + channelId);
        }

        return this.chatRepo.findByChannelIdAndCreatedAtAfter(channelId, since)
                .stream()
                .map(ChatMapper::toDTO).toList();
    }
}
