package com.mapbefine.mapbefine.common;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class TestAuthHeaderProvider {

    private static final String TOKEN_TYPE = "Bearer ";

    private JwtTokenProvider jwtTokenProvider;

    public TestAuthHeaderProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createAuthHeader(Member member) {
        Long memberId = member.getId();
        return TOKEN_TYPE + generateToken(memberId);
    }

    public String createAuthHeaderById(Long memberId) {
        return TOKEN_TYPE + generateToken(memberId);
    }

    public String createResponseAccessTokenById(Long id) {
        return generateToken(id);
    }

    private String generateToken(Long memberId) {
        return jwtTokenProvider.createToken(String.valueOf(memberId));
    }

}
