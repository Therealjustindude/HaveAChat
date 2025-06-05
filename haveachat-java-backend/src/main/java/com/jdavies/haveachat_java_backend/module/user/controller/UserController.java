package com.jdavies.haveachat_java_backend.module.user.controller;

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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


	@GetMapping("/api/user")
	public ResponseEntity<UserDTO> getAuthenticatedUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new CustomException(ErrorType.UNAUTHORIZED, "User is not authenticated.");
		}

		Object principal = authentication.getPrincipal();

		if (!(principal instanceof User)) {
			throw new CustomException(ErrorType.UNAUTHORIZED, "Invalid user principal.");
		}

		User user = (User) principal;
		UserDTO userDTO = UserMapper.toDTO(user);

		return ResponseEntity.ok(userDTO);
	}

	@GetMapping("/api/user/{id}")
	public ResponseEntity<UserDTO> getUserInfoById(@PathVariable Long id) {
			// Find the user by ID
			Optional<User> userOptional = userService.findById(id);
			User user = userOptional.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with ID: " + id));

			// convert user entity to user DTO
			UserDTO userDTO = UserMapper.toDTO(user);

			return ResponseEntity.ok(userDTO);
	}
}