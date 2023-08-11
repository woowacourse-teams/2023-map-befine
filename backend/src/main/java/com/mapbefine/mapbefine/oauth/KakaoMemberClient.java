package com.mapbefine.mapbefine.oauth;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.oauth.dto.KakaoMemberResponse;
import com.mapbefine.mapbefine.oauth.dto.KakaoToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoMemberClient implements OauthMemberClient {

    private KakaoApiClient kakaoApiClient;
    private KakaoOauthConfig kakaoOauthConfig;

    public KakaoMemberClient(
            KakaoApiClient kakaoApiClient,
            KakaoOauthConfig kakaoOauthConfig
    ) {
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoOauthConfig = kakaoOauthConfig;
    }

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public Member fetch(String authCode) {
        KakaoToken kakaoToken = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember(
                kakaoToken.tokenType() + " " + kakaoToken.accessToken()
        );

        return kakaoMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }

}
