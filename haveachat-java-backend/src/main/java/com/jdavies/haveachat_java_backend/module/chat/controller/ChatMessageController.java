package com.jdavies.haveachat_java_backend.module.chat.controller;

import com.jdavies.haveachat_java_backend.common.annotation.CurrentUser;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelMemberService;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelService;
import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.chat.service.ChatMessageService;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chatMessage")
public class ChatMessageController {

    private final ChatMessageService chatService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelMemberService channelMemberService;

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);


    @Autowired
    public ChatMessageController(ChatMessageService chatService) {
        this.chatService = chatService;
    }

    // Get message history
    @GetMapping("/history/{channelId}")
    public ResponseEntity<List<ChatMessageDTO>> getHistory(
            @PathVariable Long channelId,
            @RequestParam(required = false) Instant since,
            @CurrentUser User user
    ) {
        assertChannelExists(channelId);
        assertUserIsChannelMember(user.getId(), channelId);

        List<ChatMessageDTO> history = chatService.getHistory(channelId, since);
        return ResponseEntity.ok(history);
    }

    // Mark messages as read
    @PostMapping("/read/{channelId}")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long channelId,
            @CurrentUser User user
    ) {
        assertChannelExists(channelId);
        assertUserIsChannelMember(user.getId(), channelId);

        chatService.markMessagesAsRead(channelId, LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    // ——————————————————————————————————————————————
    // Helper functions
    // ——————————————————————————————————————————————
    private void assertChannelExists(Long channelId) {
        if (!channelService.existsByChannelId(channelId)) {
            throw new CustomException(
                    ErrorType.NOT_FOUND,
                    "Channel not found with ID: " + channelId
            );
        }
    }


    private void assertUserIsChannelMember(Long userId, Long channelId) {
        if (!channelMemberService.isUserMemberOfChannel(userId, channelId)) {
            throw new CustomException(
                    ErrorType.FORBIDDEN,
                    "User " + userId + " is not a member of channel " + channelId
            );
        }
    }

    private void assertUserIsChannelCreator(Long userId, Long channelId) {
        if (!channelService.existsByIdAndCreatorId(channelId, userId)) {
            throw new CustomException(
                    ErrorType.FORBIDDEN,
                    "User " + userId + " is not the creator of channel " + channelId
            );
        }
    }
}
