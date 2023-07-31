package com.mapbefine.mapbefine.auth.domain.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.AuthTopic;
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
    public boolean canRead(AuthTopic authTopic) {
        return true;
    }

    @Override
    public boolean canDelete(AuthTopic authTopic) {
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
