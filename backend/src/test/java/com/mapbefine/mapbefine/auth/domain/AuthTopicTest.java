package com.mapbefine.mapbefine.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthTopicTest {

    private Topic topic;
    private AuthTopic authTopic;

    @BeforeEach
    void setUp() {
        topic = TopicFixture.createByName("도이의 독서실", MemberFixture.create(Role.USER));
        authTopic = AuthTopic.from(topic);
    }

    @Test
    @DisplayName("topic의 공개 범위가 Public일때, isPrivate은 false를 반환한다.")
    void isPrivate() {
        //when
        boolean aPrivate = authTopic.isPrivate();

        //then
        assertThat(aPrivate).isFalse();
    }

    @Test
    @DisplayName("topic의 공개 범위가 Public일때, isPublic은 true를 반환한다.")
    void isPublic() {
        //when
        boolean aPublic = authTopic.isPublic();

        //then
        assertThat(aPublic).isTrue();
    }

    @Test
    @DisplayName("topic의 권한이 allMembers일때, isAllMembers는 true를 반환한다.")
    void isAllMembers() {
        //when
        boolean allMembers = authTopic.isAllMembers();

        //then
        assertThat(allMembers).isTrue();
    }

    @Test
    @DisplayName("topic의 권한이 GroupOnly일때, isGroupOnly는 false를 반환한다.")
    void isGroupOnly() {
        //when
        boolean groupOnly = authTopic.isGroupOnly();

        //then
        assertThat(groupOnly).isFalse();
    }
}
