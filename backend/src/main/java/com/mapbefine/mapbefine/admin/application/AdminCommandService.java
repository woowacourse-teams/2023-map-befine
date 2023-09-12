package com.mapbefine.mapbefine.admin.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberNotFoundException;
import com.mapbefine.mapbefine.permission.exception.PermissionErrorCode;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import org.springframework.stereotype.Service;

@Service
public class AdminCommandService {

    private final MemberRepository memberRepository;

    public AdminCommandService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // TODO: 2023/09/12 블랙리스트..?
    public void deleteMember(AuthMember authMember, Long memberId) {
        Member admin = findMemberById(authMember.getMemberId());
        validateAdminPermission(admin);

        memberRepository.deleteById(memberId);
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
