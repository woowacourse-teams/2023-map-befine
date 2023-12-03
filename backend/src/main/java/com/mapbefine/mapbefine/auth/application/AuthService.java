package com.mapbefine.mapbefine.auth.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.auth.exception.AuthErrorCode;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.mapbefine.mapbefine.auth.exception.AuthException.AuthUnauthorizedException;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PermissionRepository permissionRepository;

    public AuthService(MemberRepository memberRepository, PermissionRepository permissionRepository) {
        this.memberRepository = memberRepository;
        this.permissionRepository = permissionRepository;
    }

    public void validateMember(Long memberId) {
        if (Objects.isNull(memberId) || !memberRepository.existsById(memberId)) {
            throw new AuthUnauthorizedException(AuthErrorCode.ILLEGAL_MEMBER_ID);
        }
    }

    public AuthMember findAuthMemberByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("findAuthMemberByMemberId; memberId= " + memberId));
        if (member.isNormalStatus()) {
            return convertToAuthMember(member);
        }
        throw new AuthUnauthorizedException(AuthErrorCode.ILLEGAL_MEMBER_ID);
    }

    private AuthMember convertToAuthMember(Member member) {
        if (member.isUser()) {
            return new User(
                    member.getId(),
                    getCreatedTopics(member),
                    getTopicsWithPermission(member)
            );
        }

        return new Admin(member.getId());
    }

    private List<Long> getTopicsWithPermission(Member member) {
        return permissionRepository.findAllTopicIdsByMemberId(member.getId());
    }

    private List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopics()
                .stream()
                .map(Topic::getId)
                .toList();
    }

}
