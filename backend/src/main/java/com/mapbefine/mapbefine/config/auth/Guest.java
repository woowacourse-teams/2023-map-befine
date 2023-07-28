package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.dto.AuthTopic;
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
    public boolean canRead(AuthTopic authTopic) {
        return isPublic(publicity);
    }

    @Override
    public boolean canDelete(AuthTopic authTopic) {
        return false;
    }

    @Override
    public boolean canTopicCreate(AuthTopic authTopic) {
        return false;
    }

    @Override
    public boolean canTopicUpdate(AuthTopic authTopic) {
        return false;
    }

    @Override
    public boolean canPinCreateOrUpdate(AuthTopic authTopic) {
        return false;
    }

}
