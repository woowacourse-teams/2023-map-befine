package com.mapbefine.mapbefine.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mapbefine.mapbefine.auth.domain.token.RefreshTokenRepository;
import com.mapbefine.mapbefine.auth.dto.AccessToken;
import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TokenServiceTest {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenService tokenService;

    @Test
    void reissueAccessToken_success() {
        // given
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        Long memberId = 1L;
        String newAccessToken = "newAccessToken";

        // when
        when(refreshTokenRepository.findMemberIdByToken(refreshToken)).thenReturn(memberId);
        when(jwtTokenProvider.createAccessToken(String.valueOf(memberId))).thenReturn(newAccessToken);

        AccessToken result = tokenService.reissueAccessToken(refreshToken, accessToken);

        // then
        verify(jwtTokenProvider).validateTokensForReissue(refreshToken, accessToken);
        verify(refreshTokenRepository).findMemberIdByToken(refreshToken);
        verify(jwtTokenProvider).createAccessToken(String.valueOf(memberId));

        assertThat(newAccessToken).isEqualTo(result.accessToken());
    }

    @Test
    void removeRefreshToken_success() {
        // given
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        String payload = "1";
        Long memberId = Long.valueOf(payload);

        // when
        when(jwtTokenProvider.getPayload(accessToken)).thenReturn(payload);

        tokenService.removeRefreshToken(refreshToken, accessToken);

        // then
        verify(jwtTokenProvider).validateTokensForRemoval(refreshToken, accessToken);
        verify(jwtTokenProvider).getPayload(accessToken);
        verify(refreshTokenRepository).deleteByMemberId(memberId);
    }
}
