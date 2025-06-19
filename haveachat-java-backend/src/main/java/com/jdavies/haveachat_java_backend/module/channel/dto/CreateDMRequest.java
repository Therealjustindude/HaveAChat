package com.jdavies.haveachat_java_backend.module.channel.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class CreateDMRequest {
    private List<Long> userIds;

    private JsonNode initialMessage;

    public CreateDMRequest() {
    }

    // getters & setters
    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public JsonNode getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(JsonNode initialMessage) {
        this.initialMessage = initialMessage;
    }
}

