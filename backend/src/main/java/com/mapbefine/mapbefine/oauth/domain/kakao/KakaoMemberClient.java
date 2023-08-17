package com.mapbefine.mapbefine.oauth.domain.kakao;

import com.mapbefine.mapbefine.oauth.domain.OauthMember;
import com.mapbefine.mapbefine.oauth.domain.OauthMemberClient;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import com.mapbefine.mapbefine.oauth.domain.kakao.dto.KakaoMemberResponse;
import com.mapbefine.mapbefine.oauth.domain.kakao.dto.KakaoToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoMemberClient implements OauthMemberClient {

    private KakaoApiClient kakaoApiClient;
    private KakaoOauthProperties kakaoOauthProperties;

    public KakaoMemberClient(
            KakaoApiClient kakaoApiClient,
            KakaoOauthProperties kakaoOauthProperties
    ) {
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoOauthProperties = kakaoOauthProperties;
    }

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public OauthMember fetch(String authCode) {
        KakaoToken kakaoToken = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember(
                kakaoToken.tokenType() + " " + kakaoToken.accessToken()
        );

        return kakaoMemberResponse.extract();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthProperties.clientId());
        params.add("redirect_uri", kakaoOauthProperties.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthProperties.clientSecret());
        return params;
    }

}
