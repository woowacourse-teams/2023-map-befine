package com.mapbefine.mapbefine.auth.application;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.auth.domain.token.RefreshTokenRepository;
import com.mapbefine.mapbefine.auth.dto.AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AccessToken reissueAccessToken(String refreshToken, String accessToken) {
        jwtTokenProvider.validateTokensForReissue(refreshToken, accessToken);

        Long memberId = refreshTokenRepository.findMemberIdByToken(refreshToken);
        String reissuedToken = jwtTokenProvider.createAccessToken(String.valueOf(memberId));

        return new AccessToken(reissuedToken);
    }

    public void removeRefreshToken(String refreshToken, String accessToken) {
        jwtTokenProvider.validateTokensForRemoval(refreshToken, accessToken);

        String payload = jwtTokenProvider.getPayload(accessToken);
        Long memberId = Long.valueOf(payload);

        refreshTokenRepository.deleteByMemberId(memberId);
    }

}
