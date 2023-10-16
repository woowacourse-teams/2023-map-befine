package com.mapbefine.mapbefine.auth.domain.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicStatus;
import java.util.Collections;

public class Guest extends AuthMember {

    public Guest() {
        super(
                null,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    @Override
    public boolean canRead(Topic topic) {
        TopicStatus topicStatus = topic.getTopicStatus();
        return topicStatus.isPublic();
    }

    @Override
    public boolean canDelete(Topic topic) {
        return false;
    }

    @Override
    public boolean canTopicUpdate(Topic topic) {
        return false;
    }

    @Override
    public boolean canPinCreateOrUpdate(Topic topic) {
        return false;
    }

    @Override
    public boolean canPinCommentCreate(Topic topic) {
        return false;
    }

}