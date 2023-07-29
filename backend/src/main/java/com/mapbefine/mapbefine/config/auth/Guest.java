package com.mapbefine.mapbefine.config.auth;

public class Guest extends AuthMember {


    public Guest() {
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
    public void canDelete(AuthTopic authTopic) {
        throw new IllegalArgumentException("권한이 없습니다.");
    }

    @Override
    public void canTopicCreate() {
        throw new IllegalArgumentException("권한이 없습니다.");
    }

    @Override
    public void canTopicUpdate(AuthTopic authTopic) {
        throw new IllegalArgumentException("권한이 없습니다.");
    }

    @Override
    public void canPinCreateOrUpdate(AuthTopic authTopic) {
        throw new IllegalArgumentException("권한이 없습니다.");
    }

}
