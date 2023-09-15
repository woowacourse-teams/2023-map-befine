package com.mapbefine.mapbefine.oauth.application;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.oauth.domain.AuthCodeRequestUrlProviderComposite;
import com.mapbefine.mapbefine.oauth.domain.OauthMember;
import com.mapbefine.mapbefine.oauth.domain.OauthMemberClientComposite;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import org.springframework.stereotype.Service;

@Service
public class OauthService {

    private final MemberRepository memberRepository;
    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;

    public OauthService(
            MemberRepository memberRepository,
            AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite,
            OauthMemberClientComposite oauthMemberClientComposite
    ) {
        this.memberRepository = memberRepository;
        this.authCodeRequestUrlProviderComposite = authCodeRequestUrlProviderComposite;
        this.oauthMemberClientComposite = oauthMemberClientComposite;
    }

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public MemberDetailResponse login(OauthServerType oauthServerType, String code) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, code);
        Member savedMember = memberRepository.findByOauthId(oauthMember.getOauthId())
                .orElseGet(() -> register(oauthMember));

        return MemberDetailResponse.from(savedMember);
    }

    private Member register(OauthMember oauthMember) {

        return memberRepository.save(oauthMember.toRegisterMember());
    }

}
