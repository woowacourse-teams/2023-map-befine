package com.mapbefine.mapbefine.admin.application;

import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberNotFoundException;
import com.mapbefine.mapbefine.permission.exception.PermissionErrorCode;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminQueryService {

    private final MemberRepository memberRepository;

    public AdminQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<AdminMemberResponse> findAllMemberDetails(AuthMember authMember) {
        Member admin = findMemberById(authMember.getMemberId());
        validateAdminPermission(admin);

        List<Member> members = memberRepository.findAllByMemberInfoRole(Role.USER);

        return members.stream()
                .map(AdminMemberResponse::from)
                .toList();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND, id));
    }

    private void validateAdminPermission(Member member) {
        if (member.isAdmin()) {
            return;
        }

        throw new PermissionForbiddenException(PermissionErrorCode.PERMISSION_FORBIDDEN_BY_NOT_ADMIN);
    }

    public AdminMemberDetailResponse findMemberDetail(AuthMember authMember, Long memberId) {
        Member admin = findMemberById(authMember.getMemberId());

        validateAdminPermission(admin);

        Member findMember = findMemberById(memberId);
        List<Topic> topics = findMember.getCreatedTopics();
        List<Pin> pins = findMember.getCreatedPins();

        return AdminMemberDetailResponse.of(findMember, topics, pins);
    }
}
