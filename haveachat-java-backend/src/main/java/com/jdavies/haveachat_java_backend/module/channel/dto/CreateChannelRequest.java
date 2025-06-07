package com.jdavies.haveachat_java_backend.module.channel.dto;


public class CreateChannelRequest {
    private String name;

    private String description;          // optional

    private Boolean isPrivate = false;   // default if omitted

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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
