package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.entity.topic.Publicity;

public class Guest extends AuthMember {


    protected Guest() {
        super(
                null,
                null,
                null
        );
    }

    @Override
    public boolean canRead(Long topicId, Publicity publicity) {
        return isPublic(publicity);
    }

    @Override
    public boolean canDelete(Long topicId, Publicity publicity) {
        return false;
    }

    @Override
    public boolean canTopicCreate(Long topicId, Publicity publicity) {
        return false;
    }

    @Override
    public boolean canTopicUpdate(Long topicId, Publicity publicity) {
        return false;
    }

    @Override
    public boolean canPinCreateOrUpdate(Long topicId, Publicity publicity) {
        return false;
    }

}
