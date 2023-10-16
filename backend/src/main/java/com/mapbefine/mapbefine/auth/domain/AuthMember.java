package com.mapbefine.mapbefine.auth.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import java.util.Objects;

public abstract class AuthMember {

    protected Long memberId;
    protected List<Long> createdTopic;
    protected List<Long> topicsWithPermission;

    protected AuthMember(
            Long memberId,
            List<Long> createdTopic,
            List<Long> topicsWithPermission
    ) {
        this.memberId = memberId;
        this.createdTopic = createdTopic;
        this.topicsWithPermission = topicsWithPermission;
    }

    public abstract boolean canRead(Topic topic);

    public abstract boolean canDelete(Topic topic);

    public abstract boolean canTopicUpdate(Topic topic);

    public abstract boolean canPinCreateOrUpdate(Topic topic);

    public abstract boolean canPinCommentCreate(Topic topic);
    
    public Long getMemberId() {
        return memberId;
    }

    public boolean isSameMember(Long memberId) {
        return Objects.equals(memberId, this.memberId);
    }

}
