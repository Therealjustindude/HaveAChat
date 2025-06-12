package com.jdavies.haveachat_java_backend.module.user.controller;

import com.jdavies.haveachat_java_backend.common.annotation.CurrentUser;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.dto.UserDTO;
import com.jdavies.haveachat_java_backend.module.user.mapper.UserMapper;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<UserDTO> getAuthenticatedUserInfo(@CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "User is not authenticated.");
        }

        UserDTO userDTO = UserMapper.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<UserDTO> getUserInfoById(@PathVariable Long id) {
        // Find the user by ID
        User user = userService.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with ID: " + id));

        // convert user entity to user DTO
        UserDTO userDTO = UserMapper.toDTO(user);

        return ResponseEntity.ok(userDTO);
    }
}