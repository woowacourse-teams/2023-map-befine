package com.mapbefine.mapbefine.auth.presentation;


import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.auth.application.TokenService;
import com.mapbefine.mapbefine.auth.dto.AccessToken;
import com.mapbefine.mapbefine.auth.dto.LoginTokens;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.oauth.application.OauthService;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


class LoginControllerTest extends RestDocsIntegration {

    @MockBean
    private TokenService tokenService;
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
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth/kakao"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("소셜 로그인 성공시 로그인한 유저 정보 반환")
    void login() throws Exception {
        // given
        String code = "auth_code";

        MemberDetailResponse memberDetailResponse = new MemberDetailResponse(
                Long.MAX_VALUE,
                "모험가03fcb0d",
                "yshert@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                LocalDateTime.now()
        );

        LoginTokens loginTokens = new LoginTokens(
                testAuthHeaderProvider.createResponseAccessTokenById(Long.MAX_VALUE),
                testAuthHeaderProvider.createRefreshToken()
        );

        given(oauthService.login(KAKAO, code)).willReturn(memberDetailResponse);
        given(tokenService.issueTokens(memberDetailResponse.id())).willReturn(loginTokens);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth/login/kakao")
                        .param("code", code))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }


    @Test
    @DisplayName("만료된 Access Token과 유효한 RefreshToken인 경우, 새로운 토큰들을 발행한다.")
    void reissueTokens() throws Exception {
        AccessToken expiredAccessToken = new AccessToken("expired-access-token");
        String refreshToken = "refresh-token";

        LoginTokens reissuedTokens = new LoginTokens("reissued-access-token", "reissued-refresh-token");

        given(tokenService.reissueToken(any(), any())).willReturn(reissuedTokens);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/refresh-token")
                        .cookie(new Cookie("refresh-token", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expiredAccessToken)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("유효한 Access Token과 유효한 RefreshToken으로 로그아웃을 요청한다.")
    void logout() throws Exception {
        AccessToken accessToken = new AccessToken("access-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                        .cookie(new Cookie("refresh-token", testAuthHeaderProvider.createRefreshToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessToken)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }
}
