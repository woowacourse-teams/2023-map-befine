package com.mapbefine.mapbefine.permission.domain;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PermissionTest {

    @Test
    @DisplayName("정상적인 값을 입력하면 객체가 생성된다.")
    void createPermission() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.ADMIN
        );
        Topic topic = TopicFixture.createByName("topic", member);

        // when
        Permission permission = Permission.of(topic.getId(), member.getId());

        // then
        assertThat(permission.getTopicId()).isEqualTo(topic.getId());
        assertThat(permission.getMemberId()).isEqualTo(member.getId());
    }

}
