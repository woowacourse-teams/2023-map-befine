package com.mapbefine.mapbefine.auth.domain.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.AuthTopic;
import java.util.Collections;

public class Guest extends AuthMember {

    public Guest() {
        super(
                null,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    @Override
    public boolean canRead(AuthTopic authTopic) {
        return authTopic.isPublic();
    }

    @Override
    public boolean canDelete(AuthTopic authTopic) {
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
