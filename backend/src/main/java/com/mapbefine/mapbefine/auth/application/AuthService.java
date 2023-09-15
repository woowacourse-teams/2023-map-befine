package com.mapbefine.mapbefine.auth.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean isMember(Long memberId) {
        if (Objects.isNull(memberId)) {
            return false;
        }
        return memberRepository.existsById(memberId);
    }

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

    public boolean isAdmin(Long memberId) {
        if (Objects.isNull(memberId)) {
            return false;
        }

        Optional<Member> member = memberRepository.findById(memberId);

        return member.map(Member::isAdmin)
                .orElse(false);
    }

}
