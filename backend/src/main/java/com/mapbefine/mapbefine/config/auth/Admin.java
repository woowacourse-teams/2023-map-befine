package com.mapbefine.mapbefine.config.auth;

public class Admin extends AuthMember {

    public Admin(Long memberId) { // 준팍 여기에 memberId 는 넣어야지 찾을 수 있더라고요
        super(
                memberId,
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
