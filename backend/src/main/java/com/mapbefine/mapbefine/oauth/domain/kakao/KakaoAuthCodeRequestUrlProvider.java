package com.mapbefine.mapbefine.oauth.domain.kakao;

import com.mapbefine.mapbefine.oauth.domain.AuthCodeRequestUrlProvider;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final Logger log = LoggerFactory.getLogger(KakaoAuthCodeRequestUrlProvider.class);
    private final KakaoOauthProperties kakaoOauthProperties;

    public KakaoAuthCodeRequestUrlProvider(KakaoOauthProperties kakaoOauthProperties) {
        this.kakaoOauthProperties = kakaoOauthProperties;
        log.debug("client_id: {}", kakaoOauthProperties.redirectUri());
    }

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthProperties.clientId())
                .queryParam("redirect_uri", kakaoOauthProperties.redirectUri())
                .queryParam("scope", String.join(",", kakaoOauthProperties.scope()))
                .toUriString();
    }

}
