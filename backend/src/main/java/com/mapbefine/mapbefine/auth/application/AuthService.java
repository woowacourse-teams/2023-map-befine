package com.mapbefine.mapbefine.auth.application;

import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
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

    public Member checkLoginMember(AuthInfo authInfo) {
        return memberRepository.findByEmail(authInfo.email())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
    }

}
