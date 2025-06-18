package com.jdavies.haveachat_java_backend.module.channel.dto;

import com.jdavies.haveachat_java_backend.module.channel.util.ChannelType;

public class CreateChannelRequest {
    private String name;

    private String description;

    private boolean privateChannel = false;

    private ChannelType type = ChannelType.PUBLIC;

    public CreateChannelRequest() {
    }

    // getters & setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPrivateChannel() {
        return privateChannel;
    }

    public void setPrivateChannel(boolean privateChannel) {
        this.privateChannel = privateChannel;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }
}
