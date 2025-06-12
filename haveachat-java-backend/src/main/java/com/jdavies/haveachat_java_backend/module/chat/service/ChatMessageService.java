package com.jdavies.haveachat_java_backend.module.chat.service;

import com.jdavies.haveachat_java_backend.module.channel.service.ChannelService;
import com.jdavies.haveachat_java_backend.module.chat.mapper.ChatMessageMapper;
import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.chat.model.ChatMessage;
import com.jdavies.haveachat_java_backend.module.chat.repository.ChatMessageRepository;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);
    private final ChatMessageRepository chatRepo;
    private final ChannelService channelService;
    private final UserService userService;

    @Autowired
    public ChatMessageService(
            ChannelService channelService,
            UserService userService,
            ChatMessageRepository chatRepo
    ) {
        this.chatRepo= chatRepo;
        this.channelService = channelService;
        this.userService = userService;
    }

    /**
     * Fetch message history for a channel, optionally since a given timestamp or ID.
     */
    public List<ChatMessageDTO> getHistory(Long channelId, Instant since) {
        if (!channelService.existsByChannelId(channelId)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Channel not found: " + channelId);
        }

        if (since == null) {
            return this.chatRepo.findByChannelId(channelId)
                    .stream()
                    .map(ChatMessageMapper::toDTO)
                    .toList();
        } else {
            return this.chatRepo.findByChannelIdAndCreatedAtAfter(channelId, since)
                    .stream()
                    .map(ChatMessageMapper::toDTO)
                    .toList();
        }
    }

    public ChatMessageDTO saveChatMessage(ChatMessageDTO chatDto) {
        // Validate channel exists
        if (!channelService.existsByChannelId(chatDto.getChannelId())) {
            throw new CustomException(ErrorType.NOT_FOUND, "Channel not found: " + chatDto.getChannelId());
        }

        // Validate user exists
        if (!userService.existsByUserId(chatDto.getUserId())) {
            throw new CustomException(ErrorType.NOT_FOUND, "User not found: " + chatDto.getUserId());
        }

        // Create entity
        ChatMessage chat = new ChatMessage(chatDto.getUserId(), chatDto.getChannelId());
        chat.setMessage(chatDto.getMessage());

        // Save to DB
        ChatMessage saved = chatRepo.save(chat);

        // Return DTO (enriched with ID + timestamp)
        return ChatMessageMapper.toDTO(saved);
    }

    public void markMessagesAsRead(Long channelId, Long userId, LocalDateTime readAt) {
        // Validate channel exists
        if (!channelService.existsByChannelId(channelId)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Channel not found: " + channelId);
        }

        // Validate user exists
        if (!userService.existsByUserId(userId)) {
            throw new CustomException(ErrorType.NOT_FOUND, "User not found: " + userId);
        }

        List<ChatMessage> unreadMessages = chatRepo.findByChannelIdAndReadAtIsNull(channelId);

        for (ChatMessage chat : unreadMessages) {
            chat.setReadAt(readAt);
        }
        chatRepo.saveAll(unreadMessages);
    }

    public boolean existsByChatId(Long chatId) {
        return chatRepo.existsById(chatId);
    }


}
