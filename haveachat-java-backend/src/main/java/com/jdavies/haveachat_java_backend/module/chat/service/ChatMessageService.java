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
import java.util.Optional;

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
        // Create entity
        ChatMessage chat = new ChatMessage(chatDto.getUserId(), chatDto.getChannelId());
        chat.setMessage(chatDto.getMessage());

        // Save to DB
        ChatMessage saved = chatRepo.save(chat);

        // Return DTO (enriched with ID + timestamp)
        return ChatMessageMapper.toDTO(saved);
    }

    public void markMessagesAsRead(Long channelId, LocalDateTime readAt) {
        List<ChatMessage> unreadMessages = chatRepo.findByChannelIdAndReadAtIsNull(channelId);
        if (unreadMessages.isEmpty()) {
            return; // nothing to do — quietly exit
        }

        unreadMessages.forEach(msg -> msg.setReadAt(readAt));
        chatRepo.saveAll(unreadMessages);
    }

    public void deleteMessage(Long messageId) {
        ChatMessage message = chatRepo.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Message not found"));

        chatRepo.delete(message);
        // No broadcasting here!
    }

    public boolean existsByChatId(Long chatId) {
        return chatRepo.existsById(chatId);
    }

    public ChatMessage getByChatMessageIdOrThrow(Long messageId) {
        return chatRepo.findById(messageId).orElseThrow(() -> new CustomException(
                ErrorType.NOT_FOUND,
                "Chat message not found with ID: " + messageId
        ));
    }
}
