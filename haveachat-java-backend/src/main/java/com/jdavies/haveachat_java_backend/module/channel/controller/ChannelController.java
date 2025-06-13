package com.jdavies.haveachat_java_backend.module.channel.controller;

import com.jdavies.haveachat_java_backend.common.annotation.CurrentUser;
import com.jdavies.haveachat_java_backend.module.channel.dto.AddChannelMemberRequest;
import com.jdavies.haveachat_java_backend.module.channel.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.module.channel.model.Channel;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelMemberService;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelService;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import com.jdavies.haveachat_java_backend.module.user.dto.UserDTO;
import com.jdavies.haveachat_java_backend.module.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelMemberService channelMemberService;

    private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

    // ——————————————————————————————————————————————
    // Channel
    // ——————————————————————————————————————————————
    @PostMapping("")
    public ResponseEntity<Channel> createChannel(
            @RequestBody CreateChannelRequest req,
            @CurrentUser User user
    ) {
        Channel createdChannel = channelService.createChannel(req, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }


    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(
            @PathVariable Long channelId,
            @CurrentUser User user
    ) {
        Channel channel = channelService.getChannelById(channelId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.NOT_FOUND,
                        "Channel not found with ID: " + channelId
                ));

        assertUserIsChannelMember(user.getId(), channelId);

        return ResponseEntity.ok(channel);
    }

    // ——————————————————————————————————————————————
    // “Membership”
    // ——————————————————————————————————————————————
    @GetMapping("/{channelId}/members")
    public ResponseEntity<List<UserDTO>> listMembers(@PathVariable Long channelId, @CurrentUser User user) {
        Channel channel = channelService.getChannelById(channelId)
                .orElseThrow(() -> new CustomException(
                        ErrorType.NOT_FOUND,
                        "Channel not found with ID: " + channelId
                ));

        if (channel.getPrivateChannel()) {
            assertUserIsChannelMember(user.getId(), channelId);
        }

        List<UserDTO> members = channelService.listMembers(channelId);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/{channelId}/members")
    public ResponseEntity<Void> addMember(
            @PathVariable Long channelId,
            @RequestBody AddChannelMemberRequest req,
            @CurrentUser User user
    ) {
        assertChannelExists(channelId);
        assertUserIsChannelMember(user.getId(), channelId);

        channelService.addMember(channelId, req.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{channelId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long channelId,
            @PathVariable Long userId,
            @CurrentUser User user
    ) {
        assertChannelExists(channelId);
        assertUserIsChannelMember(userId, channelId);
        assertUserIsChannelCreator(user.getId(), channelId);

        channelService.removeMember(channelId, userId);
        return ResponseEntity.noContent().build();
    }


    // ——————————————————————————————————————————————
    // Helper functions
    // ——————————————————————————————————————————————
    private void assertChannelExists(Long channelId) {
        if (!channelService.existsByChannelId(channelId)) {
            throw new CustomException(
                    ErrorType.NOT_FOUND,
                    "Channel not found with ID: " + channelId
            );
        }
    }

    private void assertUserIsChannelMember(Long userId, Long channelId) {
        if (!channelMemberService.isUserMemberOfChannel(userId, channelId)) {
            throw new CustomException(
                    ErrorType.FORBIDDEN,
                    "User " + userId + " is not a member of channel " + channelId
            );
        }
    }

    private void assertUserIsChannelCreator(Long userId, Long channelId) {
        if (!channelService.existsByIdAndCreatorId(channelId, userId)) {
            throw new CustomException(
                    ErrorType.FORBIDDEN,
                    "User " + userId + " is not the creator of channel " + channelId
            );
        }
    }
}
