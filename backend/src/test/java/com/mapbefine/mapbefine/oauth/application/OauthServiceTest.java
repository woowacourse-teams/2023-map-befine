package com.mapbefine.mapbefine.oauth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import com.mapbefine.mapbefine.oauth.domain.kakao.KakaoOauthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OauthServiceTest {

    @Autowired
    private OauthService oauthService;

    @MockBean
    private KakaoOauthProperties kakaoOauthProperties;

    @BeforeEach
    void beforeEach() {
        given(kakaoOauthProperties.clientId()).willReturn("774");
        given(kakaoOauthProperties.scope()).willReturn(new String[] {"profile_nickname", "account_email"});
        given(kakaoOauthProperties.redirectUri()).willReturn("http://localhost:3000/oauth/redirected/kakao");
        given(kakaoOauthProperties.clientSecret()).willReturn("4521");
    }

    @Test
    @DisplayName("Kakao 의 로그인 페이지 url 을 반환한다.")
    void getAuthCodeRequestUrl_success() {
        // when
        String url = oauthService.getAuthCodeRequestUrl(OauthServerType.KAKAO);

        // then
        assertThat(url).isEqualTo("https://kauth.kakao.com/oauth/authorize?"
                + "response_type=code"
                + "&client_id=" + kakaoOauthProperties.clientId()
                + "&redirect_uri=" + kakaoOauthProperties.redirectUri()
                + "&scope=" + String.join(",", kakaoOauthProperties.scope()));
    }

}
