package com.jdavies.haveachat_java_backend.module.chat.mapper;
import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.chat.model.ChatMessage;

public class ChatMessageMapper {
    public static ChatMessageDTO toDTO(ChatMessage chat) {
        if (chat == null) return null;

        ChatMessageDTO dto = new ChatMessageDTO();

        dto.setId(chat.getId());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setModifiedAt(chat.getModifiedAt());
        dto.setChannelId(chat.getChannelId());
        dto.setUserId(chat.getUserId());
        dto.setMessage(chat.getMessage());

        return dto;
    }
}

