package com.jdavies.haveachat_java_backend.controller;

import com.jdavies.haveachat_java_backend.dto.UserDTO;
import com.jdavies.haveachat_java_backend.mapper.UserMapper;
import com.jdavies.haveachat_java_backend.model.User;
import com.jdavies.haveachat_java_backend.service.UserService;
import com.jdavies.haveachat_java_backend.exception.ErrorType;
import com.jdavies.haveachat_java_backend.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

	@GetMapping("/api/user")
    public ResponseEntity<User>  getAuthenticatedUserInfo() {
        OAuth2User oauth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (oauth2User == null) {
			throw new CustomException(ErrorType.UNAUTHORIZED, "User is not authenticated.");
		}

        String googleId = oauth2User.getAttribute("sub");
		
		// Find the user by Google ID
		Optional<User> user = userService.findByGoogleId(googleId);

		return user
				.map(ResponseEntity::ok)  // If user exists, return OK with user
				.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with Google ID: " + googleId));
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