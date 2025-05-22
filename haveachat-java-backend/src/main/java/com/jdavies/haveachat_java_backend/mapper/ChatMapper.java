package com.jdavies.haveachat_java_backend.mapper;
import com.jdavies.haveachat_java_backend.dto.ChatDTO;
import com.jdavies.haveachat_java_backend.model.Chat;

public class ChatMapper {
    public static ChatDTO toDTO(Chat chat) {
        if (chat == null) {
            return null;
        }

        ChatDTO dto = new ChatDTO();

        // copy the simple scalar fields
        dto.setId(chat.getId());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setModifiedAt(chat.getModifiedAt());

        // copy the message JSON
        dto.setMessage(chat.getMessage());

        // navigate relationships to pull out only the IDs/names you want
        if (chat.getChannel() != null) {
            dto.setChannelId(chat.getChannel().getId());
        }
        if (chat.getUser() != null) {
            dto.setUserId(chat.getUser().getId());
            dto.setUserName(chat.getUser().getName());
        }

        return dto;
    }
}

