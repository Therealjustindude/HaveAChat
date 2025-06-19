package com.jdavies.haveachat_java_backend.module.channel.service;

import com.jdavies.haveachat_java_backend.module.channel.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.module.channel.dto.CreateDMRequest;
import com.jdavies.haveachat_java_backend.module.channel.model.Channel;
import com.jdavies.haveachat_java_backend.module.channel.model.ChannelMember;
import com.jdavies.haveachat_java_backend.module.channel.repository.ChannelRepository;
import com.jdavies.haveachat_java_backend.module.channel.util.ChannelType;
import com.jdavies.haveachat_java_backend.module.chat.dto.ChatMessageDTO;
import com.jdavies.haveachat_java_backend.module.chat.model.ChatMessage;
import com.jdavies.haveachat_java_backend.module.chat.service.ChatMessageService;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.dto.UserDTO;
import com.jdavies.haveachat_java_backend.module.user.mapper.UserMapper;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import com.jdavies.haveachat_java_backend.module.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberService channelMemberService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

    @Autowired
    public ChannelService(ChannelRepository channelRepository, UserService userService, ChannelMemberService channelMemberService, ChatMessageService chatMessageService) {
        this.channelRepository = channelRepository;
        this.channelMemberService = channelMemberService;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
    }

    public List<Channel> getChannelsForUser(User user) {
        List<Long> channelIds = channelMemberService.getChannelIdsForUser(user.getId());
        return channelRepository.findAllById(channelIds);
    }

    //    createChannel - desc, name, isPrivate, creator(user)
    public Channel createChannel(CreateChannelRequest req, User user) {
        if (req.getType() != ChannelType.DM) {
            if (req.getName() == null || req.getName().isBlank()) {
                throw new CustomException(ErrorType.BAD_REQUEST, "Channel name is required");
            }

            if (channelRepository.existsByName(req.getName())) {
                throw new CustomException(ErrorType.CONFLICT, "Channel name already exists");
            }
        }

        User creator = this.userService.findByEmail(user.getEmail()).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "User not found with userEmail: " + user.getEmail()));

        Channel channel = new Channel();

        channel.setName(req.getName());
        channel.setType(req.getType());
        channel.setDescription(req.getDescription());
        channel.setPrivateChannel(req.getPrivateChannel());
        channel.setCreatorId(creator.getId());

        channelRepository.save(channel);

        ChannelMember channelMember = new ChannelMember(creator.getId(), channel.getId());
        this.channelMemberService.addMember(channelMember);

        return channel;
    }

    public Channel findOrCreateCourseChannel(String courseName) {
        if (channelRepository.existsByName(courseName)) {
            throw new CustomException(ErrorType.CONFLICT, "Channel name already exists");
        }

        Channel channel = new Channel();
        channel.setName(courseName);
        channel.setType(ChannelType.COURSE);
        channel.setPrivateChannel(false);
        channel.setCreatorId(0L);
        channel.setDescription(
                "Auto-generated channel for course: " + courseName + "\n\n" +
                        "🏌️‍♂️ Course Chat Rules:\n" +
                        "• Be respectful to others\n" +
                        "• Keep it golf-related ⛳️\n" +
                        "• No spam or self-promotion\n" +
                        "• Have fun and help each other improve!"
        );

        channelRepository.save(channel);
        return channel;
    }

    public Channel createDMChannel(CreateDMRequest req, User creator) {
        if (req.getUserIds() == null || req.getUserIds().isEmpty()) {
            throw new CustomException(ErrorType.BAD_REQUEST, "Must include at least one user in a DM");
        }

        Channel dmChannel = new Channel();
        dmChannel.setType(ChannelType.DM);
        dmChannel.setPrivateChannel(true);
        dmChannel.setCreatorId(creator.getId());
        channelRepository.save(dmChannel);

        // Add creator and other users to the DM
        Set<Long> allUserIds = new HashSet<>(req.getUserIds());
        allUserIds.add(creator.getId());
        allUserIds.forEach(userId -> {
            channelMemberService.addMember(new ChannelMember(userId, dmChannel.getId()));
        });

        // Optionally create initial message
        if (req.getInitialMessage() != null && !req.getInitialMessage().isNull()) {
            ChatMessageDTO chatDto = new ChatMessageDTO();
            chatDto.setUserId(creator.getId());
            chatDto.setChannelId(dmChannel.getId());
            chatDto.setMessage(req.getInitialMessage());

            chatMessageService.saveChatMessage(chatDto);
        }

        return dmChannel;
    }

    public void addMember(Long channelId, Long userId) {
        Channel channel = this.channelRepository.findById(channelId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));
        ;
        User user = this.userService.findById(userId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "user not found with ID: " + userId));

        ChannelMember channelMember = new ChannelMember(user.getId(), channel.getId());
        this.channelMemberService.addMember(channelMember);
    }

    public void removeMember(Long channelId, Long userId) {
        this.channelRepository.findById(channelId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        this.userService.findById(userId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "user not found with ID: " + userId));

        boolean existed = this.channelMemberService.isUserMemberOfChannel(userId, channelId);
        if (!existed) {
            throw new CustomException(ErrorType.NOT_FOUND, "Membership not found for user " + userId + " in channel " + channelId);
        }

        this.channelMemberService.removeMember(userId, channelId);
    }

    public List<UserDTO> listMembers(Long channelId) {
        this.channelRepository.findById(channelId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        return this.channelMemberService.getMembersByChannelId(channelId).stream().map(ChannelMember::getUserId).map(userId -> this.userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId))).map(UserMapper::toDTO).toList();
    }

    public boolean existsByChannelId(Long channelId) {
        return channelRepository.existsById(channelId);
    }

    public boolean existsByIdAndCreatorId(Long channelId, Long creatorId) {
        return channelRepository.existsByIdAndCreatorId(channelId, creatorId);
    }

    public Optional<Channel> getChannelById(Long channelId) {
        return channelRepository.findById(channelId);
    }
}
