package com.mapbefine.mapbefine.oauth.application;

import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberForbiddenException;
import com.mapbefine.mapbefine.oauth.domain.AuthCodeRequestUrlProviderComposite;
import com.mapbefine.mapbefine.oauth.domain.OauthMember;
import com.mapbefine.mapbefine.oauth.domain.OauthMemberClientComposite;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
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
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, code);
        Member savedMember = memberRepository.findByOauthId(oauthMember.getOauthId())
                .orElseGet(() -> register(oauthMember));

        validateMemberStatus(savedMember);

        String accessToken = jwtTokenProvider.createToken(String.valueOf(savedMember.getId()));

        return LoginInfoResponse.of(accessToken, savedMember);
    }

    private Member register(OauthMember oauthMember) {

        return memberRepository.save(oauthMember.toRegisterMember());
    }

    private void validateMemberStatus(Member member) {
        if (member.isNormalStatus()) {
            return;
        }

        throw new MemberForbiddenException(MemberErrorCode.FORBIDDEN_MEMBER_STATUS, member.getId());
    }

}
