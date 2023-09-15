package com.mapbefine.mapbefine.auth.infrastructure;

public interface TokenProvider {

    String createAccessToken(String payload);

    String createRefreshToken();

    String getPayload(String token);

    void validateTokensForReissue(String refreshToken, String accessToken);

    void validateTokensForRemoval(String refreshToken, String accessToken);

    void validateAccessToken(String accessToken);
}
