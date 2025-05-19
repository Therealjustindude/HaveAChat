package com.jdavies.haveachat_java_backend.controller;

import com.jdavies.haveachat_java_backend.dto.CreateChannelRequest;
import com.jdavies.haveachat_java_backend.exception.CustomException;
import com.jdavies.haveachat_java_backend.exception.ErrorType;
import com.jdavies.haveachat_java_backend.model.Channel;
import com.jdavies.haveachat_java_backend.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody CreateChannelRequest req) {
        Channel createdChannel = channelService.createChannel(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @PostMapping("/{channelId}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long channelId, @RequestBody Long userId) {
        channelService.addMember(channelId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(@PathVariable Long channelId) {
        Optional<Channel> channelOptional = channelService.findById(channelId);
        Channel channel = channelOptional.orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND, "Channel not found with ID: " + channelId));

        return ResponseEntity.ok(channel);
    }
}
