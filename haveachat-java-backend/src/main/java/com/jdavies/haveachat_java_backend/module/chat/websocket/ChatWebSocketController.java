package com.jdavies.haveachat_java_backend.module.chat.websocket;

import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final ChatMessageHandler handler;

    private final UserService userService;

    public ChatWebSocketController(ChatMessageHandler handler, UserService userService) {
        this.handler = handler;
        this.userService = userService;
    }

    @MessageMapping("/chat.sendMessage") // /app/chat.sendMessage
    public void sendMessage(@Payload ChatMessageDTO message,
                            @Header("simpSessionId") String sessionId,
                            Principal principal) {
        // Assert user is authenticated
        String userEmail =  getUserEmailFromPrincipalOrThrow(principal);
        // Assert user exists
        assertUserExistsByEmail(userEmail);

        handler.process(message, sessionId);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, Principal principal) {
        // Assert user is authenticated
        String userEmail =  getUserEmailFromPrincipalOrThrow(principal);
        // Assert user exists
        assertUserExistsByEmail(userEmail);

        handler.delete(id);
        return ResponseEntity.ok().build();
    }

    private String getUserEmailFromPrincipalOrThrow(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isEmpty()) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "User is not authenticated");
        }
        return principal.getName();
    }

    private void assertUserExistsByEmail(String userEmail) {
        if(!userService.existsByEmail(userEmail)) {
            throw new CustomException(ErrorType.NOT_FOUND, "User not found");
        }
    }
}
