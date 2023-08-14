package com.mapbefine.mapbefine.oauth.domain.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOauthProperties(
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
