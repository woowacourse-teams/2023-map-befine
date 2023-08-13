package com.mapbefine.mapbefine.oauth.application.presentation;

import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.oauth.application.OauthService;
import com.mapbefine.mapbefine.oauth.dto.LoginInfoResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class OauthControllerTest extends RestDocsIntegration {

    @MockBean
    private OauthService oauthService;

    @Test
    @DisplayName("kakao 소셜 로그인 Redirection Url 반환")
    void redirection() throws Exception {
        // given
        given(oauthService.getAuthCodeRequestUrl(KAKAO)).willReturn(
                "https://kauth.kakao.com/oauth/authorize?"
                + "response_type=code"
                + "&client_id={client_id}"
                + "&redirect_uri={redirection_uri}"
                + "&scope={scope}"
        );

        // when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/oauth/kakao")
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("소셜 로그인 성공시 로그인한 유저 정보 반환")
    void login() throws Exception {
        // given
        String code = "auth_code";
        LoginInfoResponse response = new LoginInfoResponse(
                testAuthHeaderProvider.createAuthHeaderById(Long.MAX_VALUE),
                new MemberDetailResponse(
                        Long.MAX_VALUE,
                        "모험가03fcb0d",
                        "yshert@naver.com",
                        "https://map-befine-official.github.io/favicon.png",
                        LocalDateTime.now()
                )
        );
        given(oauthService.login(KAKAO, code)).willReturn(response);

        // when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/oauth/login/kakao")
                        .param("code", code)
        ).andDo(restDocs.document());
    }

}
