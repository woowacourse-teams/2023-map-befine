package com.mapbefine.mapbefine.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberCommandServiceTest {

    @Autowired
    private MemberCommandService memberCommandService;

    @Autowired
    private MemberQueryService memberQueryService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Admin 이 권한을 주는 경우 정상적으로 권한이 주어진다.")
    void saveMemberTopicPermissionByAdmin() {
        // given
        Member admin = memberRepository.save(MemberFixture.create(Role.ADMIN));
        Member member = memberRepository.save(MemberFixture.create(Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", admin));
        AuthMember authAdmin = AuthMember.from(admin);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authAdmin, request);
        MemberDetailResponse memberDetailResponse = memberQueryService.findMemberTopicPermissionById(savedId);

        // then
        assertThat(memberDetailResponse.id()).isEqualTo(member.getId());
        assertThat(memberDetailResponse.name()).isEqualTo(member.getMemberInfo().getName());
        assertThat(memberDetailResponse.email()).isEqualTo(member.getMemberInfo().getEmail());
        assertThat(memberDetailResponse.imageUrl()).isEqualTo(member.getMemberInfo().getImageUrl());
    }

    @Test
    @DisplayName("Topic 의 Creator 이 권한을 주는 경우 정상적으로 권한이 주어진다.")
    void saveMemberTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(MemberFixture.create(Role.USER));
        Member member = memberRepository.save(MemberFixture.create(Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = AuthMember.from(creator);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authCreator, request);
        MemberDetailResponse memberDetailResponse = memberQueryService.findMemberTopicPermissionById(savedId);

        // then
        assertThat(memberDetailResponse.id()).isEqualTo(member.getId());
        assertThat(memberDetailResponse.name()).isEqualTo(member.getMemberInfo().getName());
        assertThat(memberDetailResponse.email()).isEqualTo(member.getMemberInfo().getEmail());
        assertThat(memberDetailResponse.imageUrl()).isEqualTo(member.getMemberInfo().getImageUrl());
    }

    @Test
    @DisplayName("Creator 가 아닌 유저가 권한을 주려는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByUser() {
        // given
        Member creator = memberRepository.save(MemberFixture.create(Role.USER));
        Member notCreator = memberRepository.save(MemberFixture.create(Role.USER));
        Member member = memberRepository.save(MemberFixture.create(Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authNotCreator = AuthMember.from(notCreator);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when then
        assertThatThrownBy(() -> memberCommandService.saveMemberTopicPermission(authNotCreator, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("본인에게 권한을 주려하는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByCreator_whenSelf_thanFail() {
        // given
        Member creator = memberRepository.save(MemberFixture.create(Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = AuthMember.from(creator);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                creator.getId()
        );

        // when then
        assertThatThrownBy(() -> memberCommandService.saveMemberTopicPermission(authCreator, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 권한을 부여 받은 사람에게 권한을 주는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByCreator__whenDuplicate_thanFail() {
        // given
        Member creator = memberRepository.save(MemberFixture.create(Role.USER));
        Member member = memberRepository.save(MemberFixture.create(Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = AuthMember.from(creator);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );
        memberCommandService.saveMemberTopicPermission(authCreator, request);

        // when then
        assertThatThrownBy(() -> memberCommandService.saveMemberTopicPermission(authCreator, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
