package com.jdavies.haveachat_java_backend.module.user.service;

import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import com.jdavies.haveachat_java_backend.module.user.repository.UserRepository;
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
        return this.userRepository.findByGoogleId(googleId);
    }

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    // This method will create a new user if they don't already exist
    public void createOrUpdateUserOAuth(String googleId, String name, String email) {
        LocalDateTime currTime = LocalDateTime.now();

        Optional<User> userOptional = userRepository.findByGoogleId(googleId);

        userOptional.ifPresentOrElse(existingUser -> {
            // If user exists, update last login time
            existingUser.setLastLogin(currTime);
            try {
                User updatedUser = this.userRepository.save(existingUser);
                logger.info("User with ID: {} logged in. Updating last login time.", updatedUser.getId());
            } catch (DataIntegrityViolationException e) {
                logger.error("Failed to update user with Google ID: {} due to a database error.", googleId, e);
                throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to update user due to a database error.");
            }
        }, () -> {
            // If user doesn't exist, create a new one
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setLastLogin(currTime);
            try {
                User savedUser = this.userRepository.save(newUser);
                logger.info("New user with ID: {} created. Storing user info.", savedUser.getId());
            } catch (DataIntegrityViolationException e) {
                logger.error("Failed to create new user with Google ID: {} due to a database error.", googleId, e);
                throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to create new user due to a database error.");
            }
        });
    }
}