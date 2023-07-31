package com.mapbefine.mapbefine.auth.domain.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.AuthTopic;
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
        return authTopic.isPublic() || isGroup(authTopic.getTopicId());
    }

    @Override
    public boolean canDelete(AuthTopic authTopic) {
        return authTopic.isPrivate() && isCreator(authTopic.getTopicId());
    }

    @Override
    public boolean canTopicUpdate(AuthTopic authTopic) {
        return isCreator(authTopic.getTopicId());
    }

    @Override
    public boolean canPinCreateOrUpdate(AuthTopic authTopic) {
        return authTopic.isAllMembers() || hasPermission(authTopic.getTopicId());
    }

    private boolean isCreator(Long topicId) {
        return createdTopic.contains(topicId);
    }

    private boolean isGroup(Long topicId) {
        return isCreator(topicId) || hasPermission(topicId);
    }

    private boolean hasPermission(Long topicId) {
        return topicsWithPermission.contains(topicId);
    }

}
