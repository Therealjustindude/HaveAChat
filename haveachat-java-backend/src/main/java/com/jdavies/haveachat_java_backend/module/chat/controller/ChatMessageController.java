package com.jdavies.haveachat_java_backend.module.chat.controller;

import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.chat.service.ChatMessageService;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chatMessage")
public class ChatMessageController {

    private final ChatMessageService chatService;

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);


    @Autowired
    public ChatMessageController(ChatMessageService chatService) {
        this.chatService = chatService;
    }

    // Get message history
    @GetMapping("/history/{channelId}")
    public ResponseEntity<List<ChatMessageDTO>> getHistory(
            @PathVariable Long channelId,
            @RequestParam(required = false) Instant since
    ) {
        List<ChatMessageDTO> history = chatService.getHistory(channelId, since);
        return ResponseEntity.ok(history);
    }

    // Mark messages as read
    @PostMapping("/read/{channelId}")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long channelId,
            Principal principal
    ) {
        if (chatService.existsByChatId(channelId)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Chat does not exist");
        }
        User user = null;


        if (principal instanceof UsernamePasswordAuthenticationToken token) {
            Object userPrincipal = token.getPrincipal();
            logger.info("token.getPrincipal(): {}", userPrincipal);

            if (userPrincipal instanceof User u) {
                user = u;
            } else {
                throw new CustomException(ErrorType.UNAUTHORIZED, "Unable to extract user from principal");
            }
        }

        if (user == null || user.getId() == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "User is missing or invalid");
        }

        chatService.markMessagesAsRead(channelId, user.getId(), LocalDateTime.now());
        return ResponseEntity.ok().build();
    }
}
