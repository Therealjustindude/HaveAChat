package com.jdavies.haveachat_java_backend.module.chat.websocket;

import com.jdavies.haveachat_java_backend.module.chat.dto.TypingDTO;
import com.jdavies.haveachat_java_backend.module.chat.model.TypingNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class TypingWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public TypingWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.typing")
    public void userTyping(@Payload TypingDTO typing, Principal principal) {
        messagingTemplate.convertAndSend(
                "/topic/channel/" + typing.getChannelId() + "/typing",
                new TypingNotification(typing.getUserId(), typing.getChannelId(), typing.getIsTyping())
        );
    }
}
