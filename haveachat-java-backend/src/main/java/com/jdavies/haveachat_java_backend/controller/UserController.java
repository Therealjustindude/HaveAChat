package com.jdavies.haveachat_java_backend.controller;

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

    @GetMapping("/user")
    public ResponseEntity<User>  getUserInfo() {
        OAuth2User oauth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String googleId = oauth2User.getAttribute("sub");
		
				// Find the user by Google ID
				Optional<User> user = userService.findByGoogleId(googleId);

				return user
						.map(ResponseEntity::ok)  // If user exists, return OK with user
						.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with Google ID: " + googleId)); 
		}

		@GetMapping("/user/{id}")
		public ResponseEntity<User> getUserInfoById(@PathVariable Long id) {
				// Find the user by ID
				Optional<User> user = userService.findById(id);

				// If user exists, return their data
				return user
						.map(ResponseEntity::ok)  // If user exists, return OK with user
						.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with ID: " + id));  // If not, throw an exception
		}
}