package com.jdavies.haveachat_java_backend.module.user.service;

import com.jdavies.haveachat_java_backend.module.common.oauth.AuthProvider;
import com.jdavies.haveachat_java_backend.module.common.oauth.OAuthUserInfo;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import com.jdavies.haveachat_java_backend.module.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<User> findByOauthIdAndProvider(String oauthId, AuthProvider provider) {
        return userRepository.findByOauthIdAndProvider(oauthId, provider);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean existsByUserId(Long id) {
        return userRepository.existsById(id);
    }

    // Create or update user based on OAuth info
    public User createOrUpdateUserOAuth(OAuthUserInfo userInfo) {
        LocalDateTime currTime = LocalDateTime.now();

        Optional<User> userOptional = findByOauthIdAndProvider(userInfo.getOauthId(), userInfo.getProvider());

        return userOptional.map(existingUser -> {
            existingUser.setLastLogin(currTime);
            User updatedUser = userRepository.save(existingUser);
            logger.info("User with ID: {} logged in. Updating last login time.", updatedUser.getId());
            return updatedUser;
        }).orElseGet(() -> {
            User newUser = new User();
            newUser.setOauthId(userInfo.getOauthId());
            newUser.setProvider(userInfo.getProvider());
            newUser.setName(userInfo.getName());
            newUser.setEmail(userInfo.getEmail());
            newUser.setLastLogin(currTime);
            User savedUser = userRepository.save(newUser);
            logger.info("New user with ID: {} created. Storing user info.", savedUser.getId());
            return savedUser;
        });
    }
}