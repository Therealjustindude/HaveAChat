package com.jdavies.haveachat_java_backend.module.chat.websocket;

import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final ChatMessageHandler handler;

    public ChatWebSocketController(ChatMessageHandler handler) {
        this.handler = handler;
    }

    @MessageMapping("/chat.sendMessage") // /app/chat.sendMessage
    public void sendMessage(@Payload ChatMessageDTO message,
                            @Header("simpSessionId") String sessionId) {
        handler.process(message, sessionId);
    }


}
