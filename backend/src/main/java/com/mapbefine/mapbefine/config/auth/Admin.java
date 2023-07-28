package com.mapbefine.mapbefine.config.auth;

public class Admin extends AuthMember {

    protected Admin() {
        super(
                null,
                null,
                null
        );
    }

    @Override
    public boolean canRead(AuthTopic authTopic) {
        return true;
    }

    @Override
    public boolean canDelete(AuthTopic authTopic) {
        return true;
    }

    @Override
    public boolean canTopicCreate(AuthTopic authTopic) {
        return true;
    }

    @Override
    public boolean canTopicUpdate(AuthTopic authTopic) {
        return true;
    }

    @Override
    public boolean canPinCreateOrUpdate(AuthTopic authTopic) {
        return true;
    }

}
