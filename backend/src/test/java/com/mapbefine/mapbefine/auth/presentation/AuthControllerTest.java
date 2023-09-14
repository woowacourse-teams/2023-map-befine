package com.mapbefine.mapbefine.auth.presentation;


import static org.apache.http.cookie.SM.COOKIE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.auth.application.TokenService;
import com.mapbefine.mapbefine.auth.dto.AccessToken;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


class AuthControllerTest extends RestDocsIntegration {

    @MockBean
    private TokenService tokenService;

    @Test
    void reissueAccessToken() throws Exception {
        AccessToken expiredAccessToken = new AccessToken("만료된 토큰");
        AccessToken reissuedAccessToken = new AccessToken("재발급된 토큰");

        given(tokenService.reissueAccessToken(any(), any())).willReturn(reissuedAccessToken);

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/refresh-token")
                        .header(COOKIE, testAuthHeaderProvider.createRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expiredAccessToken))
        ).andDo(restDocs.document());
    }

    @Test
    void logout() throws Exception {
        AccessToken accessToken = new AccessToken("access token");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/logout")
                        .header(COOKIE, testAuthHeaderProvider.createRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessToken))
        ).andDo(restDocs.document());

    }
}
