package com.mapbefine.mapbefine.permission.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);

        // then
        assertThat(permission.getMember()).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(member);
        assertThat(permission.getTopic()).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(topic);
    }

    @Test
    @DisplayName("MemberTopicPermission 을 생성하면, Topic 과 Member 에 등록이 된다.")
    void createPermissionAssociatedWithTopicAndMember() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.ADMIN
        );
        Topic topic = TopicFixture.createByName("topic", member);

        // when
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);
        List<Topic> topicsWithPermission = member.getTopicsWithPermissions();
        List<Permission> permissions = topic.getPermissions();

        // then
        assertThat(topicsWithPermission).hasSize(1);
        assertThat(permissions).hasSize(1);
        assertThat(topicsWithPermission.get(0)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(topic);
        assertThat(permissions.get(0)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(permission);
    }

}
