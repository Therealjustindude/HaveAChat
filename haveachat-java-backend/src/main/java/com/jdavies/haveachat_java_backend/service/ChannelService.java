package com.jdavies.haveachat_java_backend.service;

import com.jdavies.haveachat_java_backend.dto.UserDTO;
import com.jdavies.haveachat_java_backend.mapper.UserMapper;
import com.jdavies.haveachat_java_backend.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.exception.CustomException;
import com.jdavies.haveachat_java_backend.exception.ErrorType;
import com.jdavies.haveachat_java_backend.model.Channel;
import com.jdavies.haveachat_java_backend.model.ChannelMember;
import com.jdavies.haveachat_java_backend.model.User;
import com.jdavies.haveachat_java_backend.repository.ChannelMemberRepository;
import com.jdavies.haveachat_java_backend.repository.ChannelRepository;
import com.jdavies.haveachat_java_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

    @Autowired
    public ChannelService(ChannelRepository channelRepository, UserRepository userRepository,  ChannelMemberRepository channelMemberRepository) {
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.userRepository = userRepository;
    }

    public Optional<Channel> findById(Long id) {
        return this.channelRepository.findById(id);
    }

    public Optional<Channel> findByName(String name) {
        return this.channelRepository.findByName(name);
    }

    //    createChannel - desc, name, isPrivate, creator(user)
    public Channel createChannel(CreateChannelRequest req, Principal principal) {
        String userEmail;

        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            userEmail = (String) attributes.get("email");
        } else {
            userEmail = principal.getName();
        }

        User creator = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with userEmail: " + userEmail));

        Channel channel = new Channel();
        channel.setName(req.getName());
        channel.setDescription(req.getDescription());
        channel.setIsPrivate(Boolean.TRUE.equals(req.getIsPrivate()));
        channel.setCreatorId(creator.getId());

        channelRepository.save(channel);

        ChannelMember channelMember = new ChannelMember(creator, channel);
        this.channelMemberRepository.save(channelMember);

        return channel;
    }

    public void addMember(Long channelId, Long userId) {
        Channel channel = this.channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));
        ;
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "user not found with ID: " + userId));

        ChannelMember channelMember = new ChannelMember(user, channel);
        this.channelMemberRepository.save(channelMember);
    }

    public void removeMember(Long channelId, Long userId) {
        this.channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "user not found with ID: " + userId));

        boolean existed = this.channelMemberRepository.existsByUserIdAndChannelId(userId, channelId);
        if (!existed) {
            throw new CustomException(
                    ErrorType.NOT_FOUND,
                    "Membership not found for user " + userId + " in channel " + channelId);
        }

        this.channelMemberRepository.deleteByUserIdAndChannelId(userId, channelId);
    }

    public List<UserDTO> listMembers(Long channelId) {
        this.channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.NOT_FOUND,
                        "Channel not found with ID: " + channelId));

        return this.channelMemberRepository
                .findByChannelId(channelId)
                .stream()
                .map(ChannelMember::getUser)
                .map(UserMapper::toDTO)
                .toList();
    }
}
