package com.mapbefine.mapbefine.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
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
        Member admin = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        Member member = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
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
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
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
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        Member notCreator = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
        Member member = memberRepository.save(MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
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
    void saveMemberTopicPermissionByCreator_whenSelf_thenFail() {
        // given
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
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
    void saveMemberTopicPermissionByCreator_whenDuplicate_thenFail() {
        // given
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
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

    @Test
    @DisplayName("Admin 이 권한을 삭제하는 경우 정상적으로 삭제가 이루어진다.")
    void deleteMemberTopicPermissionByAdmin() {
        // given
        Member admin = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        Member member = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", admin));
        AuthMember authAdmin = AuthMember.from(admin);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authAdmin, request);
        memberCommandService.deleteMemberTopicPermission(authAdmin, savedId);

        // then
        assertThat(memberRepository.existsById(savedId)).isFalse();
    }

    @Test
    @DisplayName("creator 이 권한을 삭제하는 경우 정상적으로 삭제가 이루어진다.")
    void deleteMemberTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = AuthMember.from(creator);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authCreator, request);
        memberCommandService.deleteMemberTopicPermission(authCreator, savedId);

        // then
        assertThat(memberRepository.existsById(savedId)).isFalse();
    }

    @Test
    @DisplayName("creator 가 아닌 유저가 권한을 삭제하는 경우 예외가 발생한다.")
    void deleteMemberTopicPermissionByUser() {
        // given
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        Member nonCreator = memberRepository.save(MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Member member = memberRepository.save(MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = AuthMember.from(creator);
        AuthMember authNonCreator = AuthMember.from(nonCreator);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authCreator, request);

        // then
        assertThatThrownBy(() -> memberCommandService.deleteMemberTopicPermission(authNonCreator, savedId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 권한을 삭제하려 하려는 경우 예외가 발생한다.")
    void deleteMemberTopicPermissionByCreator_whenNoneExistsPermission_thenFail() {
        // given
        Member creator = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        AuthMember authCreator = AuthMember.from(creator);

        // when then
        assertThatThrownBy(() -> memberCommandService.deleteMemberTopicPermission(authCreator, Long.MAX_VALUE))
                .isInstanceOf(NoSuchElementException.class);
    }

//    @Test
//    @DisplayName("유저를 저장한다.")
//    void save() {
//        // given
//        Member member = MemberFixture.create(
//                "member",
//                "member@naver.com",
//                Role.USER
//        );
//        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
//                member.getMemberInfo().getName(),
//                member.getMemberInfo().getEmail(),
//                member.getMemberInfo().getImageUrl(),
//                member.getMemberInfo().getRole()
//        );
//
//        // when
//        Long savedId = memberCommandService.save(memberCreateRequest);
//        Member savedResult = memberRepository.findById(savedId)
//                .orElseThrow(NoSuchElementException::new);
//
//        // then
//        assertThat(savedResult).usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(member);
//    }

    @Test
    @DisplayName("이미 존재하는 이름으로 유저를 저장할 때 예외가 발생한다.")
    void save_whenDuplicateName_thenFail() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        memberRepository.save(member);
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                member.getMemberInfo().getName(),
                "memberr@naver.com",
                member.getMemberInfo().getImageUrl(),
                member.getMemberInfo().getRole()
        );

        // when
        assertThatThrownBy(() -> memberCommandService.save(memberCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 유저를 저장할 때 예외가 발생한다.")
    void save_whenDuplicateEmail_thenFail() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        memberRepository.save(member);
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "memberr",
                member.getMemberInfo().getEmail(),
                member.getMemberInfo().getImageUrl(),
                member.getMemberInfo().getRole()
        );

        // when
        assertThatThrownBy(() -> memberCommandService.save(memberCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
