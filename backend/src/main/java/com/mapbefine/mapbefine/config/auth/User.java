package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.entity.topic.Publicity;
import java.util.List;

public class User extends AuthMember {

    protected User(
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
    public boolean canRead(Long topicId, Publicity publicity) {
        return isPublic(publicity) || isGroup(topicId);
    }

    @Override
    public boolean canDelete(Long topicId, Publicity publicity) {
        return isPrivate(publicity) && isCreator(topicId);
    }

    @Override
    public boolean canTopicCreate(Long topicId, Publicity publicity) {
        return true;
    }

    @Override
    public boolean canTopicUpdate(Long topicId, Publicity publicity) {
        return isCreator(topicId);
    }

    @Override
    public boolean canPinCreateOrUpdate(Long topicId, Publicity publicity) {
        return isPublic(publicity) || isGroup(topicId);
    }

    private boolean isGroup(Long topicId) {
        return isCreator(topicId) || hasPermission(topicId);
    }

    private boolean hasPermission(Long topicId) {
        return topicsWithPermission.contains(topicId);
    }

    private boolean isCreator(Long topicId) {
        return createdTopic.contains(topicId);
    }

}
