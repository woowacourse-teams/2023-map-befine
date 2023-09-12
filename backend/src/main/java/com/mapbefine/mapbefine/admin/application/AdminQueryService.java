package com.mapbefine.mapbefine.admin.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberNotFoundException;
import com.mapbefine.mapbefine.permission.exception.PermissionErrorCode;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminQueryService {

    private final MemberRepository memberRepository;

    public AdminQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDetailResponse> findAllMemberDetails(AuthMember authMember) {
        Member admin = findMemberById(authMember.getMemberId());
        validateAdminPermission(admin);

        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberDetailResponse::from)
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
}
