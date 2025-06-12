package com.jdavies.haveachat_java_backend.module.chat.websocket;

import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.chat.model.ChatMessage;
import com.jdavies.haveachat_java_backend.module.chat.service.ChatMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatService;

    public ChatMessageHandler(
            SimpMessagingTemplate messagingTemplate,
            ChatMessageService chatService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    public void process(ChatMessageDTO dto, String sessionId) {
        ChatMessageDTO saved = this.chatService.saveChatMessage(dto); // Save to DB
        messagingTemplate.convertAndSend("/topic/channel/" + dto.getChannelId(), saved);
    }

    public void delete(Long messageId) {
        ChatMessage message = chatService.getByChatMessageIdOrThrow(messageId);
        chatService.deleteMessage(messageId);

        // Notify all subscribers
        messagingTemplate.convertAndSend(
                "/topic/channel/" + message.getChannelId() + "/messages/deleted",
                messageId
        );
    }
}
