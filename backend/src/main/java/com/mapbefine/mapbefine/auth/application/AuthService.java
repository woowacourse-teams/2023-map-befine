package com.mapbefine.mapbefine.auth.application;

import static com.mapbefine.mapbefine.auth.exception.AuthException.AuthUnauthorizedException;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.auth.exception.AuthErrorCode;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validateMember(Long memberId) {
        if (Objects.isNull(memberId) || !memberRepository.existsById(memberId)) {
            throw new AuthUnauthorizedException(AuthErrorCode.ILLEGAL_MEMBER_ID);
        }
    }

    // TODO 테스트가 필요하긴 한데, MemberQueryService와 너무 중복되는 내용이다. 엔티티 테스트로 중복을 없앨 수 있으면 좋겠다.
    public AuthMember findAuthMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .map(this::convertToAuthMember)
                .orElseThrow(() -> new IllegalArgumentException("findAuthMemberByMemberId; memberId= " + memberId));
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
