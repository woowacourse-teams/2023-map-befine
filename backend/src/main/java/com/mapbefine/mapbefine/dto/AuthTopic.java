package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;

public record AuthTopic(
        Long topicId,
        Publicity publicity,
        Permission permission

) {
    public static AuthTopic from(Topic topic) {
        return new AuthTopic(
                topic.getId(),
                topic.getPublicity(),
                topic.getPermission()
        );
    }
}
