package com.mapbefine.mapbefine.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.admin.application.AdminCommandService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.common.exception.UnauthorizedException;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ServiceTest
class AuthServiceTest extends TestDatabaseContainer {

    @Autowired
    private AuthService authService;
    @Autowired
    private AdminCommandService adminCommandService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    private Member member;
    private Topic topicWithPermission;
    private Topic createdTopic;


    @BeforeEach
    void setUp() {
        Member admin = memberRepository.save(MemberFixture.create("admin", "admin@member.com", Role.ADMIN));
        member = memberRepository.save(MemberFixture.create("member", "member1@member.com", Role.USER));
        topicWithPermission = topicRepository.save(TopicFixture.createPrivateAndGroupOnlyTopic(admin));
        createdTopic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(admin));
        permissionRepository.save(Permission.createPermissionAssociatedWithTopicAndMember(topicWithPermission, member));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    @DisplayName("인증된 회원 정보를 가져올 때, 차단 혹은 탈퇴한 회원의 정보는 가져오지 않는다.")
    void findAuthMemberByMemberId_Success_notContainingNotNormalMember() {
        // given
        adminCommandService.blockMember(member.getId());

        // when
        // then
        assertThatThrownBy(() -> authService.findAuthMemberByMemberId(member.getId()))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    @DisplayName("인증된 회원 정보를 가져올 때, 삭제된 지도에 대해서는 권한을 부여하지 않는다. (soft delete 반영)")
    void findAuthMemberByMemberId_Success_notContainingSoftDeletedPermission() {
        // given
        adminCommandService.deleteTopic(topicWithPermission.getId());

        // when
        AuthMember authMember = authService.findAuthMemberByMemberId(member.getId());

        // then
        assertThat(authMember.canPinCreateOrUpdate(topicWithPermission)).isFalse();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    @DisplayName("인증된 회원 정보를 가져올 때, 삭제된 지도에 대해서는 생성자 여부를 확인하지 않는다. (soft delete 반영)")
    void findAuthMemberByMemberId_Success_notContainingSoftDeletedCreatedTopic() {
        // given
        adminCommandService.deleteTopic(createdTopic.getId());

        // when
        AuthMember authMember = authService.findAuthMemberByMemberId(member.getId());

        // then
        assertThat(authMember.canTopicUpdate(createdTopic)).isFalse();
    }

}
