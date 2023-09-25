package com.mapbefine.mapbefine.common;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class TestAuthHeaderProvider {

    private static final String TOKEN_TYPE = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public TestAuthHeaderProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createAuthHeader(Member member) {
        Long memberId = member.getId();
        return TOKEN_TYPE + generateAccessToken(memberId);
    }

    public String createAuthHeaderById(Long memberId) {
        return TOKEN_TYPE + generateAccessToken(memberId);
    }

    public String createResponseAccessTokenById(Long id) {
        return generateAccessToken(id);
    }

    public String createRefreshToken() {
        return jwtTokenProvider.createRefreshToken();
    }

    private String generateAccessToken(Long memberId) {
        return jwtTokenProvider.createAccessToken(String.valueOf(memberId));
    }

}
