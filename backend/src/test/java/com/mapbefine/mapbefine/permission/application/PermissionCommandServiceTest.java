package com.mapbefine.mapbefine.permission.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionBadRequestException;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PermissionCommandServiceTest {

    @Autowired
    private PermissionCommandService permissionCommandService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    @DisplayName("Admin 이 권한을 주는 경우 정상적으로 권한이 주어진다.")
    void saveMemberTopicPermissionByAdmin() {
        // given
        Member admin = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", admin));

        // when
        AuthMember authAdmin = new Admin(admin.getId());
        Long permissionTargetMemberId = member.getId();
        PermissionRequest request = new PermissionRequest(
                topic.getId(),
                List.of(permissionTargetMemberId)
        );
        permissionCommandService.addPermission(authAdmin, request);

        // then
        Member memberWithPermission = memberRepository.findById(permissionTargetMemberId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(memberWithPermission.getTopicsWithPermissions())
                .contains(topic);
    }

    @Test
    @DisplayName("Topic 의 Creator 이 권한을 주는 경우 정상적으로 권한이 주어진다.")
    void saveMemberTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));

        // when
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );
        Long permissionTargetMemberId = member.getId();
        PermissionRequest request = new PermissionRequest(
                topic.getId(),
                List.of(permissionTargetMemberId)
        );
        permissionCommandService.addPermission(authCreator, request);

        // then
        Member memberWithPermission = memberRepository.findById(permissionTargetMemberId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(memberWithPermission.getTopicsWithPermissions())
                .contains(topic);
    }

    @Test
    @DisplayName("Creator 가 아닌 유저가 권한을 주려는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByUser() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member notCreator = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));

        // when
        AuthMember authNotCreator = new User(
                notCreator.getId(),
                getCreatedTopics(notCreator),
                getTopicsWithPermission(notCreator)
        );
        PermissionRequest request = new PermissionRequest(
                topic.getId(),
                List.of(member.getId())
        );

        // then
        assertThatThrownBy(() -> permissionCommandService.addPermission(authNotCreator, request))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @Test
    @DisplayName("Guest 가 유저에게 권한을 주려는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByGuest() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));

        // when
        AuthMember guest = new Guest();
        Long savedId = member.getId();
        PermissionRequest request = new PermissionRequest(
                topic.getId(),
                List.of(savedId)
        );

        // then
        assertThatThrownBy(() -> permissionCommandService.addPermission(guest, request))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @Test
    @DisplayName("본인에게 권한을 주는 요청에 대해서는 이미 권한을 부여한 것으로 판단해 DB에 반영하지 않는다.")
    void saveSelfTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Long creatorId = creator.getId();
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        Long topicId = topic.getId();

        // when
        PermissionRequest request = new PermissionRequest(
                topicId,
                List.of(creatorId)
        );
        AuthMember authCreator = new User(
                creatorId,
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );
        permissionCommandService.addPermission(authCreator, request);

        // then
        assertThat(permissionRepository.existsByTopicIdAndMemberId(topicId, creatorId))
                .isFalse();
    }

    @Test
    @DisplayName("이미 권한을 부여 받은 사람에게 권한을 주는 경우 이미 권한을 부여한 것으로 판단한다.")
    void saveMemberTopicPermissionByCreator_whenDuplicate_thenFail() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        Member member = memberRepository.save(
                MemberFixture.create(
                        "members",
                        "members@naver.com",
                        Role.USER
                )
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        Long topicId = topic.getId();
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);
        permissionRepository.save(permission);

        // when
        PermissionRequest request = new PermissionRequest(
                topicId,
                List.of(member.getId())
        );
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // then
        assertDoesNotThrow(() -> permissionCommandService.addPermission(authCreator, request));
    }

    @Test
    @DisplayName("Admin 이 권한을 삭제하는 경우 정상적으로 삭제가 이루어진다.")
    void deleteMemberTopicPermissionByAdmin() {
        // given
        Member admin = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", admin));
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = permissionRepository.save(permission).getId();

        // when
        AuthMember authAdmin = new Admin(admin.getId());
        permissionCommandService.deleteMemberTopicPermission(authAdmin, savedId);

        // then
        assertThat(permissionRepository.existsById(savedId)).isFalse();
    }

    @Test
    @DisplayName("creator 이 권한을 삭제하는 경우 정상적으로 삭제가 이루어진다.")
    void deleteMemberTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // when
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = permissionRepository.save(permission).getId();
        permissionCommandService.deleteMemberTopicPermission(authCreator, savedId);

        // then
        assertThat(permissionRepository.existsById(savedId)).isFalse();
    }

    @Test
    @DisplayName("creator 가 아닌 유저가 권한을 삭제하는 경우 예외가 발생한다.")
    void deleteMemberTopicPermissionByUser() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member nonCreator = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authNonCreator = new User(
                nonCreator.getId(),
                getCreatedTopics(nonCreator),
                getTopicsWithPermission(nonCreator)
        );

        // when
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = permissionRepository.save(permission).getId();

        // then
        assertThatThrownBy(() -> permissionCommandService.deleteMemberTopicPermission(authNonCreator, savedId))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 권한을 삭제하려 하려는 경우 예외가 발생한다.")
    void deleteMemberTopicPermissionByCreator_whenNoneExistsPermission_thenFail() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // when then
        assertThatThrownBy(() -> permissionCommandService.deleteMemberTopicPermission(authCreator, Long.MAX_VALUE))
                .isInstanceOf(PermissionBadRequestException.class);
    }

    private List<Long> getTopicsWithPermission(Member member) {
        return member.getTopicsWithPermissions()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    private List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopics()
                .stream()
                .map(Topic::getId)
                .toList();
    }

}
