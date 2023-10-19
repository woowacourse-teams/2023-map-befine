package com.mapbefine.mapbefine.permission.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.response.PermissionMemberDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionedMemberResponse;
import com.mapbefine.mapbefine.permission.dto.response.TopicAccessDetailResponse;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionNotFoundException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PermissionQueryServiceTest extends TestDatabaseContainer {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionQueryService permissionQueryService;

    @Test
    @DisplayName("특정 지도에 대한 접근 정보(권한 회원 목록 및 공개 여부)를 모두 조회한다. 권한 회원 중 생성자는 제외한다.")
    void findTopicAccessDetailById() {
        /// TODO: 2023/09/15  리팩터링
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
        TopicAccessDetailResponse accessDetailResponse = permissionQueryService.findTopicAccessDetailById(topic1.getId());
        MemberResponse member1Response = MemberResponse.from(member1InTopic1);
        MemberResponse member2Response = MemberResponse.from(member2InTopic1);

        // then
        assertThat(accessDetailResponse.publicity()).isEqualTo(topic1.getPublicity());
        List<PermissionedMemberResponse> permissionedMembers = accessDetailResponse.permissionedMembers();
        assertThat(permissionedMembers).hasSize(2)
                .extracting(PermissionedMemberResponse::memberResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(member1Response, member2Response));
        assertThat(permissionedMembers)
                .extracting(PermissionedMemberResponse::memberResponse)
                .map(MemberResponse::id)
                .doesNotContain(topic1.getCreator().getId());
    }

    @Test
    @DisplayName("ID 를 통해서 토픽에 권한이 있는자를 조회한다.")
    void findPermissionById() {
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
        PermissionMemberDetailResponse permissionMemberDetailResponse =
                permissionQueryService.findPermissionById(savedId);
        MemberDetailResponse permissionUserResponse = MemberDetailResponse.from(permissionUser);

        // then
        assertThat(permissionMemberDetailResponse)
                .extracting(PermissionMemberDetailResponse::memberDetailResponse)
                .usingRecursiveComparison()
                .isEqualTo(permissionUserResponse);
    }

    @Test
    @DisplayName("ID 를 통해서 토픽에 권한이 있는자를 조회하려 할 때, 결과가 존재하지 않을 때 예외가 발생한다.")
    void findPermissionById_whenNoneExistsPermission_thenFail() {
        // given when then
        assertThatThrownBy(() -> permissionQueryService.findPermissionById(Long.MAX_VALUE))
                .isInstanceOf(PermissionNotFoundException.class);
    }


}
