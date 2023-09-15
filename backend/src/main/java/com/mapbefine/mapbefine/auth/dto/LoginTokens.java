package com.mapbefine.mapbefine.auth.dto;

public record LoginTokens(
        String accessToken,
        String refreshToken
) {

    public AccessToken toAccessToken() {
        return new AccessToken(accessToken);
    }
}
