package com.jdavies.haveachat_java_backend.module.channel.service;

import com.jdavies.haveachat_java_backend.module.channel.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.module.channel.model.Channel;
import com.jdavies.haveachat_java_backend.module.channel.model.ChannelMember;
import com.jdavies.haveachat_java_backend.module.channel.repository.ChannelRepository;
import com.jdavies.haveachat_java_backend.module.user.dto.UserDTO;
import com.jdavies.haveachat_java_backend.module.user.mapper.UserMapper;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberService channelMemberService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

    @Autowired
    public ChannelService(ChannelRepository channelRepository, UserService userService, ChannelMemberService channelMemberService) {
        this.channelRepository = channelRepository;
        this.channelMemberService = channelMemberService;
        this.userService = userService;
    }

    //    createChannel - desc, name, isPrivate, creator(user)
    public Channel createChannel(CreateChannelRequest req, Principal principal) {
        if (channelRepository.existsByName(req.getName())) {
            throw new CustomException(ErrorType.CONFLICT, "Channel name already exists");
        }

        String userEmail;

        if (principal instanceof UsernamePasswordAuthenticationToken token) {
            Object userPrincipal = token.getPrincipal();
            logger.info("token.getPrincipal(): {}", token.getPrincipal());

            if (userPrincipal instanceof User user) {
                userEmail = user.getEmail();
            } else if (userPrincipal instanceof String username) {
                userEmail = username;
            } else {
                throw new CustomException(ErrorType.UNAUTHORIZED, "Unable to extract user email from principal");
            }
        } else {
            userEmail = null;
        }

        if (userEmail == null || userEmail.isBlank()) {
            throw new CustomException(ErrorType.UNAUTHORIZED, "User email is missing or invalid");
        }

        User creator = this.userService.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with userEmail: " + userEmail));

        Channel channel = new Channel();
        channel.setName(req.getName());
        channel.setDescription(req.getDescription());
        channel.setPrivateChannel(Boolean.TRUE.equals(req.getPrivateChannel()));
        channel.setCreatorId(creator.getId());

        channelRepository.save(channel);

        ChannelMember channelMember = new ChannelMember(creator.getId(), channel.getId());
        this.channelMemberService.addMember(channelMember);

        return channel;
    }

    public void addMember(Long channelId, Long userId) {
        Channel channel = this.channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));
        ;
        User user = this.userService.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "user not found with ID: " + userId));

        ChannelMember channelMember = new ChannelMember(user.getId(), channel.getId());
        this.channelMemberService.addMember(channelMember);
    }

    public void removeMember(Long channelId, Long userId) {
        this.channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        this.userService.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "user not found with ID: " + userId));

        boolean existed = this.channelMemberService.isUserMemberOfChannel(userId, channelId);
        if (!existed) {
            throw new CustomException(
                    ErrorType.NOT_FOUND,
                    "Membership not found for user " + userId + " in channel " + channelId);
        }

        this.channelMemberService.removeMember(userId, channelId);
    }

    public List<UserDTO> listMembers(Long channelId) {
        this.channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.NOT_FOUND,
                        "Channel not found with ID: " + channelId));

        return this.channelMemberService
                .getMembersByChannelId(channelId)
                .stream()
                .map(ChannelMember::getUserId)
                .map(userId -> this.userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId)))
                .map(UserMapper::toDTO)
                .toList();
    }

    public boolean isExistingChannel(Long channelId) {
        return channelRepository.existsById(channelId);
    }

    public Optional<Channel> getChannelById(Long channelId) {
        return channelRepository.findById(channelId);
    }
}
