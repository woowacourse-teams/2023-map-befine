package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
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
    public boolean canRead(AuthTopic authTopic) {
        return isPublic(authTopic.publicity()) || isGroup(authTopic.topicId());
    }

    @Override
    public void canDelete(AuthTopic authTopic) {
        if (isPrivate(authTopic.publicity()) && isCreator(authTopic.topicId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    @Override
    public void canTopicCreate() {
    }

    @Override
    public void canTopicUpdate(AuthTopic authTopic) {
        if (isCreator(authTopic.topicId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    @Override
    public void canPinCreateOrUpdate(AuthTopic authTopic) {
        if (isAllMembers(authTopic) || hasPermission(authTopic.topicId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
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
