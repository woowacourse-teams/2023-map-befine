package com.mapbefine.mapbefine.oauth.application.presentation;

import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.oauth.application.OauthService;
import com.mapbefine.mapbefine.oauth.domain.kakao.KakaoOauthProperties;
import com.mapbefine.mapbefine.oauth.domain.kakao.dto.KakaoMemberResponse;
import com.mapbefine.mapbefine.oauth.domain.kakao.dto.KakaoToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class OauthControllerTest extends RestDocsIntegration {

    @Autowired
    private OauthService oauthService;

    @MockBean
    private KakaoOauthProperties kakaoOauthProperties;

    @MockBean
    private KakaoToken kakaoToken;

    @MockBean
    private KakaoMemberResponse kakaoMemberResponse;

    @BeforeEach
    void beforeEach() {
        given(kakaoOauthProperties.clientId()).willReturn("[client_id]");
        given(kakaoOauthProperties.scope()).willReturn(new String[] {"[scope]"});
        given(kakaoOauthProperties.redirectUri()).willReturn("[redirection_uri]");

        given(kakaoToken.tokenType()).willReturn("tokenType");
        given(kakaoToken.accessToken()).willReturn("accessToken");
    }

    @Test
    @DisplayName("Kakao Redirection Url 요청")
    void redirection() throws Exception {
        // given when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/oauth/kakao")
        ).andDo(restDocs.document());
    }

}
