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

    // TODO: 2023/11/30 간접 참조로 인한 무의미한 테스트 ?
//    @Test
//    @DisplayName("MemberTopicPermission 을 생성하면, Topic 과 Member 에 등록이 된다.")
//    void createPermissionAssociatedWithTopicAndMember() {
//        // given
//        Member member = MemberFixture.create(
//                "member",
//                "member@naver.com",
//                Role.ADMIN
//        );
//        Topic topic = TopicFixture.createByName("topic", member);
//
//        // when
//        Permission permission = Permission.of(topic.getId(), member.getId());
//        List<Topic> topicsWithPermission = member.getTopicsWithPermissions();
//        List<Permission> permissions = topic.getPermissions();
//
//        // then
//        assertThat(topicsWithPermission).hasSize(1);
//        assertThat(permissions).hasSize(1);
//        assertThat(topicsWithPermission.get(0)).usingRecursiveComparison()
//                .ignoringFields("createdAt", "updatedAt")
//                .isEqualTo(topic);
//        assertThat(permissions.iterator().next()).usingRecursiveComparison()
//                .ignoringFields("createdAt", "updatedAt")
//                .isEqualTo(permission);
//    }

}
