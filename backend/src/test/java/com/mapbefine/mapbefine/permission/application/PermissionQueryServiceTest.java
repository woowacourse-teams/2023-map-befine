package com.mapbefine.mapbefine.permission.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.response.PermissionDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PermissionQueryServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionQueryService permissionQueryService;

    @Test
    @DisplayName("Topic 에 권한이 있는자들을 모두 조회한다.")
        // creator 는 권한이 있는자들을 조회할 때 조회되어야 할 것인가??
    void findAllWithPermission() {
        // given
        Member member1InTopic1 = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Member member2InTopic1 = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER)
        );
        Member member3InTopic2 = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER)
        );
        Topic topic1 = topicRepository.save(TopicFixture.createByName("topic1", member3InTopic2));
        Topic topic2 = topicRepository.save(TopicFixture.createByName("topic2", member1InTopic1));
        permissionRepository.save(
                Permission.createPermissionAssociatedWithTopicAndMember(topic1, member1InTopic1)
        );
        permissionRepository.save(
                Permission.createPermissionAssociatedWithTopicAndMember(topic1, member2InTopic1)
        );
        permissionRepository.save(
                Permission.createPermissionAssociatedWithTopicAndMember(topic2, member3InTopic2)
        );

        // when
        List<PermissionResponse> permissionRespons = permissionQueryService.findAllTopicPermissions(topic1.getId());
        MemberResponse memberResponse1 = MemberResponse.from(member1InTopic1);
        MemberResponse memberResponse2 = MemberResponse.from(member2InTopic1);

        // then
        assertThat(permissionRespons).hasSize(2)
                .extracting(PermissionResponse::memberResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(memberResponse1, memberResponse2));
    }

    @Test
    @DisplayName("ID 를 통해서 토픽에 권한이 있는자를 조회한다.")
    void findMemberTopicPermissionById() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Member permissionUser = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER)
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        Long savedId = permissionRepository.save(
                Permission.createPermissionAssociatedWithTopicAndMember(topic, permissionUser)
        ).getId();

        // when
        PermissionDetailResponse permissionDetailResponse =
                permissionQueryService.findPermissionById(savedId);
        MemberDetailResponse permissionUserResponse = MemberDetailResponse.from(permissionUser);

        // then
        assertThat(permissionDetailResponse)
                .extracting(PermissionDetailResponse::memberDetailResponse)
                .usingRecursiveComparison()
                .isEqualTo(permissionUserResponse);
    }

    @Test
    @DisplayName("ID 를 통해서 토픽에 권한이 있는자를 조회하려 할 때, 결과가 존재하지 않을 때 예외가 발생한다.")
    void findMemberTopicPermissionById_whenNoneExistsPermission_thenFail() {
        // given when then
        assertThatThrownBy(() -> permissionQueryService.findPermissionById(Long.MAX_VALUE))
                .isInstanceOf(NoSuchElementException.class);
    }


}
