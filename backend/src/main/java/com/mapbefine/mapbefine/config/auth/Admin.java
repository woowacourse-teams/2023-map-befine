package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.dto.AuthTopic;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import java.util.List;

public class Admin extends AuthMember {

    protected Admin(
            Long memberId,
            List<Long> createdTopic,
            List<Long> topicsWithPermission
    ) {
        super(
                memberId,
                createdTopic,
                topicsWithPermission
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
