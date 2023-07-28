package com.mapbefine.mapbefine.config.auth;

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
        return isPublic(authTopic.publicity());
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
