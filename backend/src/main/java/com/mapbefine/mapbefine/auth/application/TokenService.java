package com.mapbefine.mapbefine.auth.application;

import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.ILLEGAL_TOKEN;

import com.mapbefine.mapbefine.auth.domain.token.RefreshToken;
import com.mapbefine.mapbefine.auth.domain.token.RefreshTokenRepository;
import com.mapbefine.mapbefine.auth.dto.LoginTokens;
import com.mapbefine.mapbefine.auth.exception.AuthException.AuthUnauthorizedException;
import com.mapbefine.mapbefine.auth.infrastructure.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public LoginTokens issueTokens(Long memberId) {
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = tokenProvider.createRefreshToken();

        if (refreshTokenRepository.existsByMemberId(memberId)) {
            refreshTokenRepository.deleteByMemberId(memberId);
        }

        refreshTokenRepository.save(new RefreshToken(refreshToken, memberId));

        return new LoginTokens(accessToken, refreshToken);
    }

    public LoginTokens reissueToken(String refreshToken, String accessToken) {
        tokenProvider.validateTokensForReissue(refreshToken, accessToken);
        Long memberId = findMemberIdByRefreshToken(refreshToken);

        return issueTokens(memberId);
    }

    private Long findMemberIdByRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(token)
                .orElseThrow(() -> new AuthUnauthorizedException(ILLEGAL_TOKEN));

        return refreshToken.getMemberId();
    }

    public void removeRefreshToken(String refreshToken, String accessToken) {
        tokenProvider.validateTokensForRemoval(refreshToken, accessToken);

        String payload = tokenProvider.getPayload(accessToken);
        Long memberId = Long.valueOf(payload);

        refreshTokenRepository.deleteByMemberId(memberId);
    }


}
