package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.entity.topic.Permission;
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
    public boolean canRead(AuthTopic authTopic) {
        return isPublic(authTopic.publicity()) || isGroup(authTopic.topicId());
    }

    @Override
    public boolean canDelete(AuthTopic authTopic) {
        return isPrivate(authTopic.publicity()) && isCreator(authTopic.topicId());
    }

    @Override
    public boolean canTopicCreate(AuthTopic authTopic) {
        return true;
    }

    @Override
    public boolean canTopicUpdate(AuthTopic authTopic) {
        return isCreator(authTopic.topicId());
    }

    @Override
    public boolean canPinCreateOrUpdate(AuthTopic authTopic) {
        return isAllMembers(authTopic) || hasPermission(authTopic.topicId());
    }

    private boolean isAllMembers(final AuthTopic authTopic) {
        return authTopic.permission() == Permission.ALL_MEMBERS;
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
