package com.mapbefine.mapbefine.auth.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean isMember(AuthInfo authInfo) {
        return memberRepository.existsByEmail(authInfo.email());
    }

    public AuthMember findAuthMemberByEmail(AuthInfo authInfo) {
        return memberRepository.findByEmail(authInfo.email())
                .map(this::convertToAuthMember)
                .orElseGet(Guest::new);
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
        return member.getTopicsWithPermission()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    private List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopic()
                .stream()
                .map(Topic::getId)
                .toList();
    }
}
