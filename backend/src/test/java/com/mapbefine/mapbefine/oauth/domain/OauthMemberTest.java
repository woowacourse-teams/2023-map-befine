package com.mapbefine.mapbefine.oauth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OauthMemberTest {

    @Test
    @DisplayName("소셜 로그인 닉네임의 길이가 13자 초과이면 기본 랜덤 닉네임을 부여한다.")
    void toRegisterMember() {
        OauthMember oauthMember = OauthMember.of(
                "일이삼사오육칠팔구십일이삼사",
                "member@gmail.com",
                "https://image.url",
                1L,
                OauthServerType.KAKAO
        );

        String expected = oauthMember.toRegisterMember()
                .getMemberInfo()
                .getNickName();

        System.out.println(expected);
        assertThat(expected).contains("모험가");
    }
}
