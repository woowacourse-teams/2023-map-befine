package com.mapbefine.mapbefine.oauth.application;

import com.mapbefine.mapbefine.auth.domain.token.RefreshToken;
import com.mapbefine.mapbefine.auth.domain.token.RefreshTokenRepository;
import com.mapbefine.mapbefine.auth.infrastructure.TokenProvider;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.oauth.domain.AuthCodeRequestUrlProviderComposite;
import com.mapbefine.mapbefine.oauth.domain.OauthMember;
import com.mapbefine.mapbefine.oauth.domain.OauthMemberClientComposite;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import com.mapbefine.mapbefine.oauth.dto.LoginTokens;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OauthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final RefreshTokenRepository refreshTokenRepository;

    public OauthService(
            MemberRepository memberRepository,
            TokenProvider tokenProvider,
            AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite,
            OauthMemberClientComposite oauthMemberClientComposite,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.authCodeRequestUrlProviderComposite = authCodeRequestUrlProviderComposite;
        this.oauthMemberClientComposite = oauthMemberClientComposite;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    @Transactional
    public LoginTokens login(OauthServerType oauthServerType, String code) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, code);
        Member savedMember = memberRepository.findByOauthId(oauthMember.getOauthId())
                .orElseGet(() -> register(oauthMember));
        Long savedMemberId = savedMember.getId();

        String accessToken = tokenProvider.createAccessToken(String.valueOf(savedMemberId));
        String refreshToken = tokenProvider.createRefreshToken();

        refreshTokenRepository.save(new RefreshToken(refreshToken, savedMemberId));

        return new LoginTokens(
                accessToken,
                refreshToken,
                MemberDetailResponse.from(savedMember)
        );
    }

    private Member register(OauthMember oauthMember) {

        return memberRepository.save(oauthMember.toRegisterMember());
    }

}
