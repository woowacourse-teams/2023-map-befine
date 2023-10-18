package com.mapbefine.mapbefine.auth.domain.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicStatus;
import java.util.List;

public class User extends AuthMember {

    public User(
            Long memberId,
            List<Long> createdTopic,
            List<Long> topicsWithPermission
    ) {
        super(
                memberId,
                createdTopic,
                topicsWithPermission
        );
    }

    @Override
    public boolean canRead(Topic topic) {
        TopicStatus topicStatus = topic.getTopicStatus();

        return topicStatus.isPublic() || hasPermission(topic.getId());
    }

    @Override
    public boolean canDelete(Topic topic) {
        TopicStatus topicStatus = topic.getTopicStatus();

        return topicStatus.isPrivate() && isCreator(topic.getId());
    }

    @Override
    public boolean canTopicUpdate(Topic topic) {
        return isCreator(topic.getId());
    }

    @Override
    public boolean canPinCreateOrUpdate(Topic topic) {
        TopicStatus topicStatus = topic.getTopicStatus();

        return topicStatus.isAllMembers() || hasPermission(topic.getId());
    }

    @Override
    public boolean canPinCommentCreate(Topic topic) {
        return canRead(topic);
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isUser() {
        return true;
    }

    @Override
    public boolean isGuest() {
        return false;
    }

    private boolean isCreator(Long topicId) {
        return createdTopic.contains(topicId);
    }

    private boolean hasPermission(Long topicId) {
        return isCreator(topicId) || topicsWithPermission.contains(topicId);
    }

}
