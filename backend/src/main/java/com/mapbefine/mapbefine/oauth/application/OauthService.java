package com.mapbefine.mapbefine.oauth.application;

import com.mapbefine.mapbefine.oauth.KakaoApiClient;
import com.mapbefine.mapbefine.oauth.KakaoToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class OauthService {

    private KakaoApiClient kakaoApiClient;

    public OauthService(final KakaoApiClient kakaoApiClient) {
        this.kakaoApiClient = kakaoApiClient;
    }

    public String getAuthCodeRequestUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=" + RESPONSE_TYPE
                + "&client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&scope=" + SCOPE;
    }

    public KakaoToken fetch(String code) {
        KakaoToken kakaoToken = kakaoApiClient.fetchToken(tokenRequestParams(code));

        return kakaoToken;
    }

    private MultiValueMap<String, String> tokenRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", CLIENT_SECRET);

        return params;
    }

}
