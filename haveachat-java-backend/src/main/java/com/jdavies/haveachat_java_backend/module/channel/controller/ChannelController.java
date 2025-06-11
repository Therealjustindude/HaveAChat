package com.jdavies.haveachat_java_backend.module.channel.controller;

import com.jdavies.haveachat_java_backend.module.channel.dto.AddChannelMemberRequest;
import com.jdavies.haveachat_java_backend.module.channel.model.Channel;
import com.jdavies.haveachat_java_backend.module.channel.service.ChannelService;
import com.jdavies.haveachat_java_backend.module.channel.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.module.user.dto.UserDTO;
import com.jdavies.haveachat_java_backend.module.exception.CustomException;
import com.jdavies.haveachat_java_backend.module.exception.ErrorType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

    // ——————————————————————————————————————————————
    // Channel
    // ——————————————————————————————————————————————
    @PostMapping("")
    public ResponseEntity<Channel> createChannel(@RequestBody CreateChannelRequest req, Principal principal) {
        Channel createdChannel = channelService.createChannel(req, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }


    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(@PathVariable Long channelId) {
        Optional<Channel> channelOptional = channelService.getChannelById(channelId);
        Channel channel = channelOptional.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        return ResponseEntity.ok(channel);
    }

    // ——————————————————————————————————————————————
    // “Membership”
    // ——————————————————————————————————————————————
    @PostMapping("/{channelId}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long channelId, @RequestBody AddChannelMemberRequest req) {
        channelService.addMember(channelId, req.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{channelId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long channelId,
            @PathVariable Long userId
    ) {
        channelService.removeMember(channelId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{channelId}/members")
    public ResponseEntity<List<UserDTO>> listMembers(@PathVariable Long channelId) {
        List<UserDTO> members = channelService.listMembers(channelId);
        return ResponseEntity.ok(members);
    }

}
