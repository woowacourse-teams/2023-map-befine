package com.mapbefine.mapbefine.oauth.application;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.oauth.AuthCodeRequestUrlProviderComposite;
import com.mapbefine.mapbefine.oauth.OauthMemberClientComposite;
import com.mapbefine.mapbefine.oauth.OauthServerType;
import com.mapbefine.mapbefine.oauth.dto.LoginInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class OauthService {

    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private OauthMemberClientComposite oauthMemberClientComposite;

    public OauthService(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite,
            OauthMemberClientComposite oauthMemberClientComposite
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authCodeRequestUrlProviderComposite = authCodeRequestUrlProviderComposite;
        this.oauthMemberClientComposite = oauthMemberClientComposite;
    }

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public LoginInfoResponse login(OauthServerType oauthServerType, String code) {
        // TODO: 2023/08/11 nickname 카카오에서 받은 값 그대로 쓰지 않고 UUID 사용해 unique 하게 만들기
        Member oauthMember = oauthMemberClientComposite.fetch(oauthServerType, code);
        OauthId oauthId = oauthMember.getOauthId();
        Member savedMember = memberRepository.findByOauthId(oauthId)
                .orElseGet(() -> register(oauthMember));

        String accessToken = jwtTokenProvider.createToken(String.valueOf(savedMember.getId()));

        return LoginInfoResponse.of(accessToken, savedMember);
    }

    private Member register(Member member) {
        return memberRepository.save(member);
    }

}
