package com.mapbefine.mapbefine.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.auth.domain.token.RefreshToken;
import com.mapbefine.mapbefine.auth.domain.token.RefreshTokenRepository;
import com.mapbefine.mapbefine.auth.dto.LoginTokens;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@ServiceTest
@TestPropertySource(locations = "classpath:application.yml")
class TokenServiceTest extends TestDatabaseContainer {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.access-expire-length}")
    private long accessExpirationTime;
    @Value("${security.jwt.token.refresh-expire-length}")
    private long refreshExpirationTime;

    private TestTokenProvider testTokenProvider;
    private TokenService tokenService;
    private Long memberId;

    @BeforeEach
    void setUp() {
        testTokenProvider = getTestTokenProvider();
        tokenService = new TokenService(testTokenProvider, refreshTokenRepository);

        Member member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        memberId = member.getId();
    }

    private TestTokenProvider getTestTokenProvider() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        return new TestTokenProvider(
                secretKey,
                yesterday,
                accessExpirationTime,
                refreshExpirationTime
        );
    }

    @Nested
    @DisplayName("Token을 발급할 때,")
    class IssueTokens {

        @Test
        @DisplayName("멤버 Id를 입력하면 정상적으로 토큰을 발행한다.")
        void success() {
            // given
            String payload = String.valueOf(memberId);

            // when
            LoginTokens loginTokens = tokenService.issueTokens(memberId);

            // then
            String actualAccessToken = loginTokens.accessToken();
            String actualRefreshToken = loginTokens.refreshToken();

            assertThat(actualAccessToken).isNotNull();
            assertThat(actualRefreshToken).isNotNull();
            assertThat(payload).isEqualTo(testTokenProvider.getPayload(actualAccessToken));
        }

    }


    @Nested
    @DisplayName("Token을 재발급할 때,")
    class ReissueToken {

        @Test
        @DisplayName("Refresh Token이 유효하고, AccessToken이 만료되었다면 성공한다")
        void success() {
            // given
            String payload = String.valueOf(memberId);
            String refreshToken = createRefreshToken();
            String accessToken = testTokenProvider.createExpiredAccessToken(payload);

            // when
            LoginTokens loginTokens = tokenService.reissueToken(refreshToken, accessToken);

            // then
            String actualAccessToken = loginTokens.accessToken();
            String actualRefreshToken = loginTokens.refreshToken();

            assertThat(actualAccessToken).isNotNull();
            assertThat(actualRefreshToken).isNotNull();
            assertThat(payload).isEqualTo(testTokenProvider.getPayload(actualAccessToken));
        }

    }

    @Nested
    @DisplayName("Refresh Token을 제거할 때,")
    class RemoveRefreshToken {

        @Test
        @DisplayName("Refresh Token과 AccessToken 모두 유효하다면 성공한다")
        void removeRefreshToken_success() {
            // given
            String refreshToken = createRefreshToken();
            String accessToken = testTokenProvider.createAccessToken(String.valueOf(memberId));

            // when
            tokenService.removeRefreshToken(refreshToken, accessToken);

            // then
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(refreshToken);

            assertThat(optionalRefreshToken).isEmpty();
        }

    }

    private String createRefreshToken() {
        String refreshToken = testTokenProvider.createRefreshToken();
        refreshTokenRepository.save(new RefreshToken(refreshToken, memberId));
        return refreshToken;
    }


}
