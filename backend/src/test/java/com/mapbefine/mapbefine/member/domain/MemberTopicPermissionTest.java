package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTopicPermissionTest {

    @Test
    @DisplayName("정상적인 값을 입력하면 객체가 생성된다.")
    void createMemberTopicPermission() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.ADMIN
        );
        Topic topic = TopicFixture.createByName("topic", member);

        // when
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);

        // then
        assertThat(memberTopicPermission.getMember()).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(member);
        assertThat(memberTopicPermission.getTopic()).usingRecursiveComparison()
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
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        List<Topic> topicsWithPermission = member.getTopicsWithPermissions();
        List<MemberTopicPermission> memberTopicPermissions = topic.getMemberTopicPermissions();

        // then
        assertThat(topicsWithPermission).hasSize(1);
        assertThat(memberTopicPermissions).hasSize(1);
        assertThat(topicsWithPermission.get(0)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(topic);
        assertThat(memberTopicPermissions.get(0)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(memberTopicPermission);
    }

}
