package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.entity.topic.Publicity;
import java.util.List;

public class Admin extends AuthMember {

    protected Admin(
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
        return true;
    }

    @Override
    public boolean canDelete(Long topicId, Publicity publicity) {
        return true;
    }

    @Override
    public boolean canTopicCreate(Long topicId, Publicity publicity) {
        return true;
    }

    @Override
    public boolean canTopicUpdate(Long topicId, Publicity publicity) {
        return true;
    }

    @Override
    public boolean canPinCreateOrUpdate(Long topicId, Publicity publicity) {
        return true;
    }

}
