package com.mapbefine.mapbefine.common;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class TestAccessTokenProvider {

    private JwtTokenProvider jwtTokenProvider;

    public TestAccessTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(Member member) {
        Long memberId = member.getId();
        return "Bearer " + jwtTokenProvider.createToken(String.valueOf(memberId));
    }

}
