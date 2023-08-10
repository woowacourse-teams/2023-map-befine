package com.mapbefine.mapbefine.oauth;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.mapbefine.mapbefine.oauth.dto.KakaoToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoApiClient {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoToken fetchToken(@RequestBody MultiValueMap<String, String> params);

//    @GetExchange("https://kapi.kakao.com/v2/user/me")
//    KakaoMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
