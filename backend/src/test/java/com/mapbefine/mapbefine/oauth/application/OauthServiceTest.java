package com.mapbefine.mapbefine.oauth.application;

import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.DatabaseCleanup;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.oauth.domain.OauthMember;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import com.mapbefine.mapbefine.oauth.domain.kakao.KakaoApiClient;
import com.mapbefine.mapbefine.oauth.domain.kakao.KakaoOauthProperties;
import com.mapbefine.mapbefine.oauth.domain.kakao.dto.KakaoMemberResponse;
import com.mapbefine.mapbefine.oauth.domain.kakao.dto.KakaoToken;
import com.mapbefine.mapbefine.oauth.dto.LoginInfoResponse;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OauthServiceTest {

    private static final OauthMember oauthMember = OauthMember.of(
            "yshert@naver.com",
            "https://map-befine-official.github.io/favicon.png",
            Long.MAX_VALUE,
            KAKAO
    );

    @Autowired
    private OauthService oauthService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private KakaoApiClient kakaoApiClient;

    @MockBean
    private KakaoOauthProperties kakaoOauthProperties;

    @MockBean
    private KakaoToken kakaoToken;

    @MockBean
    private KakaoMemberResponse kakaoMemberResponse;


    @BeforeEach
    void setUp() {
        given(kakaoOauthProperties.clientId()).willReturn("774");
        given(kakaoOauthProperties.scope()).willReturn(new String[] {"profile_nickname", "account_email"});
        given(kakaoOauthProperties.redirectUri()).willReturn("http://localhost:3000/oauth/redirected/kakao");
        given(kakaoOauthProperties.clientSecret()).willReturn("4521");
        
        given(kakaoToken.tokenType()).willReturn("tokenType");
        given(kakaoToken.accessToken()).willReturn("accessToken");
        
        given(kakaoMemberResponse.extract()).willReturn(oauthMember);

        given(kakaoApiClient.fetchToken(any())).willReturn(kakaoToken);
        given(kakaoApiClient.fetchMember("tokenType accessToken")).willReturn(kakaoMemberResponse);
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("Kakao 의 로그인 페이지 url 을 반환한다.")
    void getAuthCodeRequestUrl_success() {
        // when
        String url = oauthService.getAuthCodeRequestUrl(OauthServerType.KAKAO);

        // then
        assertThat(url).isEqualTo("https://kauth.kakao.com/oauth/authorize?"
                + "response_type=code"
                + "&client_id=" + kakaoOauthProperties.clientId()
                + "&redirect_uri=" + kakaoOauthProperties.redirectUri()
                + "&scope=" + String.join(",", kakaoOauthProperties.scope()));
    }
    
    @Test
    @DisplayName("Kakao 로 로그인 처음 하는 경우 사용자의 정보를 저장하고 사용자의 로그인 정보를 반환한다.")
    void loginAndRegister_success() {
        // when
        LoginInfoResponse response = oauthService.login(KAKAO, "auth");
        Member savedMember = memberRepository.findByMemberInfoEmail("yshert@naver.com")
                .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(response.accessToken()).isNotNull();
        assertThat(response.member().email()).isEqualTo("yshert@naver.com");
        assertThat(response.member().imageUrl()).isEqualTo(
                "https://map-befine-official.github.io/favicon.png"
        );
        assertThat(response.member().nickName()).startsWith("모험가");
        assertThat(savedMember.getId()).isEqualTo(response.member().id());
    }

    @Test
    @DisplayName("Kakao 로 로그인 처음이 아닌 경우 사용자의 정보를 저장하지 않고 사용자의 로그인 정보를 반환한다.")
    void login() {
        // given
        oauthService.login(KAKAO, "auth");

        // then
        // 중복으로 저장되지 않는다는 것을 assertDoesNotThrow 로 표현해봤어요..
        // findByMemberInfoEmail 했을 때 size 가 2가 아닌지 확인하는 것을 고려해봤지만
        // 현재로서는 반환 타입이 Optional<Member> 라서 검증하지 못했네용..
        assertDoesNotThrow(() -> oauthService.login(KAKAO, "auth"));
    }

}
