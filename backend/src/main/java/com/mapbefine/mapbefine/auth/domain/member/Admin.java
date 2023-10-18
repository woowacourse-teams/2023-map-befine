package com.mapbefine.mapbefine.auth.domain.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.Collections;

public class Admin extends AuthMember {

    public Admin(Long memberId) {
        super(
                memberId,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    @Override
    public boolean canRead(Topic topic) {
        return true;
    }

    @Override
    public boolean canDelete(Topic topic) {
        return true;
    }

    @Override
    public boolean canTopicUpdate(Topic topic) {
        return true;
    }

    @Override
    public boolean canPinCreateOrUpdate(Topic topic) {
        return true;
    }

    @Override
    public boolean canPinCommentCreate(Topic topic) {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public boolean isUser() {
        return false;
    }

    @Override
    public boolean isGuest() {
        return false;
    }

}
