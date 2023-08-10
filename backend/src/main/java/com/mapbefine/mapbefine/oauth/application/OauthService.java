package com.mapbefine.mapbefine.oauth.application;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.oauth.KakaoApiClient;
import com.mapbefine.mapbefine.oauth.dto.KakaoMemberResponse;
import com.mapbefine.mapbefine.oauth.dto.KakaoToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class OauthService {

    private KakaoApiClient kakaoApiClient;
    private MemberRepository memberRepository;

    public OauthService(KakaoApiClient kakaoApiClient, MemberRepository memberRepository) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberRepository = memberRepository;
    }

    public String getAuthCodeRequestUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=" + RESPONSE_TYPE
                + "&client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&scope=" + SCOPE;
    }

    public MemberDetailResponse login(String code) {
        KakaoToken kakaoToken = kakaoApiClient.fetchToken(tokenRequestParams(code));

        KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember(
                kakaoToken.tokenType() + " " + kakaoToken.accessToken()
        );

        Long oauthId = kakaoMemberResponse.id();
        Member member = memberRepository.findByOauthIdOauthServerId(oauthId)
                .orElseGet(() -> register(kakaoMemberResponse, oauthId));

        // TODO: 2023/08/10 nickname을 소셜 로그인으로 받아온 정보를 저장함으로서, nickname의 unique를 보장할 수 없어짐
        //  일부 사용자가 직접 입력하는 플로우를 추가할 필요가 있음

        return MemberDetailResponse.from(member);
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

    private Member register(KakaoMemberResponse kakaoMemberResponse, Long oauthId) {
        Member member = Member.of(
                kakaoMemberResponse.kakaoAccount().profile().nickname(),
                kakaoMemberResponse.kakaoAccount().email(),
                kakaoMemberResponse.kakaoAccount().profile().profileImageUrl(),
                Role.USER,
                new OauthId(oauthId)
        );

        return memberRepository.save(member);
    }

}
