package com.jdavies.haveachat_java_backend.module.user.mapper;

import com.jdavies.haveachat_java_backend.module.user.dto.UserDTO;
import com.jdavies.haveachat_java_backend.module.user.model.User;

public class UserMapper {
    // Convert User entity to UserDTO
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getName(), user.getEmail());
    }
}
