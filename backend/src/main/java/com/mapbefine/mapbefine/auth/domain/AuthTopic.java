package com.mapbefine.mapbefine.auth.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicStatus;

public class AuthTopic {

    private final Long topicId;
    private final TopicStatus topicStatus;

    private AuthTopic(Long topicId, TopicStatus topicStatus) {
        this.topicId = topicId;
        this.topicStatus = topicStatus;
    }

    public static AuthTopic from(Topic topic) {
        return new AuthTopic(topic.getId(), topic.getTopicStatus());
    }

    public boolean isPublic() {
        return topicStatus.isPublic();
    }

    public boolean isPrivate() {
        return topicStatus.isPrivate();
    }

    public boolean isAllMembers() {
        return topicStatus.isAllMembers();
    }

    public boolean isGroupOnly() {
        return topicStatus.isGroupOnly();
    }

    public Long getTopicId() {
        return topicId;
    }
}
