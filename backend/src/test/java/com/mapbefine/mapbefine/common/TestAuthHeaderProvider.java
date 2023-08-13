package com.mapbefine.mapbefine.common;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class TestAuthHeaderProvider {

    private JwtTokenProvider jwtTokenProvider;

    public TestAuthHeaderProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createAuthHeader(Member member) {
        Long memberId = member.getId();
        return generateToken(memberId);
    }

    public String createAuthHeaderById(Long id) {
        return generateToken(id);
    }

    private String generateToken(Long memberId) {
        return "Bearer " + jwtTokenProvider.createToken(String.valueOf(memberId));
    }

}
