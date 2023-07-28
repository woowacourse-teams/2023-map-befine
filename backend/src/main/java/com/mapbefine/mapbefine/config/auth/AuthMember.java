package com.mapbefine.mapbefine.config.auth;

import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.member.MemberTopicPermission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;
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

        return new Admin();
    }

    private static List<Long> getTopicsWithPermission(Member member) {
        return member.getTopicsWithPermission()
                .stream()
                .map(MemberTopicPermission::getTopic)
                .map(Topic::getId)
                .toList();
    }

    private static List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopic()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    public abstract boolean canRead(AuthTopic authTopic);

    public abstract boolean canDelete(AuthTopic authTopic);

    public abstract boolean canTopicCreate(AuthTopic authTopic);

    public abstract boolean canTopicUpdate(AuthTopic authTopic);

    public abstract boolean canPinCreateOrUpdate(AuthTopic authTopic);

    protected boolean isPublic(Publicity publicity) {
        return publicity == Publicity.PUBLIC;
    }

    protected boolean isPrivate(Publicity publicity) {
        return publicity == Publicity.PRIVATE;
    }

}
