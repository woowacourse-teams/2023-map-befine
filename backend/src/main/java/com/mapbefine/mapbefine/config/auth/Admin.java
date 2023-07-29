package com.mapbefine.mapbefine.config.auth;

public class Admin extends AuthMember {

    public Admin(Long memberId) { // TODO : 준팍 여기에 memberId 는 넣어야지 찾을 수 있더라고요
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
    public void canDelete(AuthTopic authTopic) {
    }

    @Override
    public void canTopicCreate() {
    }

    @Override
    public void canTopicUpdate(AuthTopic authTopic) {
    }

    @Override
    public void canPinCreateOrUpdate(AuthTopic authTopic) {
    }

}
