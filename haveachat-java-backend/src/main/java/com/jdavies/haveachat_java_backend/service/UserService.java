package com.jdavies.haveachat_java_backend.service;

import com.jdavies.haveachat_java_backend.model.User;
import com.jdavies.haveachat_java_backend.exception.ErrorType;
import com.jdavies.haveachat_java_backend.exception.CustomException;
import com.jdavies.haveachat_java_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByGoogleId(String googleId) {
				return userRepository.findByGoogleId(googleId);
		}

    public Optional<User> findById(Long id) {
				return userRepository.findById(id);
		}

    // This method will create a new user if they don't already exist
    public User createOrUpdateUser(String googleId, String name, String email) {
        LocalDateTime currTime = LocalDateTime.now();
        
        Optional<User> user = userRepository.findByGoogleId(googleId);

        return user.map(existingUser -> {
            // If user exists, update last login time
            existingUser.setLastLogin(currTime);
            try {
                User updatedUser = userRepository.save(existingUser);
                logger.info("User with ID: {} logged in. Updating last login time.", updatedUser.getId());
                return updatedUser;
            } catch (DataIntegrityViolationException e) {
                logger.error("Failed to update user with Google ID: {} due to a database error.", googleId, e);
                throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to update user due to a database error.");
            }
        }).orElseGet(() -> {
            // If user doesn't exist, create a new one
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setCreatedAt(currTime);
            newUser.setLastLogin(currTime);

            try {
                User savedUser = userRepository.save(newUser);
                logger.info("New user with ID: {} created. Storing user info.", savedUser.getId());
                return savedUser;
            } catch (DataIntegrityViolationException e) {
                logger.error("Failed to create new user with Google ID: {} due to a database error.", googleId, e);
                throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to create new user due to a database error.");
            }
        });
    }
}