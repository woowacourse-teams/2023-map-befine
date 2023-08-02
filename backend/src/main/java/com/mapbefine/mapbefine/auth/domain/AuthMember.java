package com.mapbefine.mapbefine.auth.domain;

import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;

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

    public static AuthMember from(Member member) {
        if (member == null) {
            return new Guest();
        }

        if (member.isUser()) {
            return new User(member.getId(), getCreatedTopics(member), getTopicsWithPermission(member));
        }

        return new Admin(member.getId());
    }

    private static List<Long> getTopicsWithPermission(Member member) {
        return member.getTopicsWithPermission()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    private static List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopic()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    public abstract boolean canRead(Topic topic);

    public abstract boolean canDelete(Topic topic);

    public abstract boolean canTopicUpdate(Topic topic);

    public abstract boolean canPinCreateOrUpdate(Topic topic);

    public Long getMemberId() {
        return memberId;
    }
}
