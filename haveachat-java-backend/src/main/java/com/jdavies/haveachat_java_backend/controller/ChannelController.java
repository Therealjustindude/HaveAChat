package com.jdavies.haveachat_java_backend.controller;

import com.jdavies.haveachat_java_backend.dto.UserDTO;
import com.jdavies.haveachat_java_backend.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.exception.CustomException;
import com.jdavies.haveachat_java_backend.exception.ErrorType;
import com.jdavies.haveachat_java_backend.model.Channel;
import com.jdavies.haveachat_java_backend.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    // ——————————————————————————————————————————————
    // Channel
    // ——————————————————————————————————————————————
    @PostMapping("/api/channels")
    public ResponseEntity<Channel> createChannel(@RequestBody CreateChannelRequest req) {
        Channel createdChannel = channelService.createChannel(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @GetMapping("/api/channels/{channelId}")
    public ResponseEntity<Channel> getChannel(@PathVariable Long channelId) {
        Optional<Channel> channelOptional = channelService.findById(channelId);
        Channel channel = channelOptional.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        return ResponseEntity.ok(channel);
    }

    // ——————————————————————————————————————————————
    // “Membership”
    // ——————————————————————————————————————————————
    @PostMapping("/api/channels/{channelId}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long channelId, @RequestBody Long userId) {
        channelService.addMember(channelId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/{channelId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long channelId,
            @PathVariable Long userId
    ) {
        channelService.removeMember(channelId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/{channelId}/members")
    public ResponseEntity<List<UserDTO>> listMembers(@PathVariable Long channelId) {
        List<UserDTO> members = channelService.listMembers(channelId);
        return ResponseEntity.ok(members);
    }

}
