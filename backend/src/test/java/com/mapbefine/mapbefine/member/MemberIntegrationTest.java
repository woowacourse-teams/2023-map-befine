package com.mapbefine.mapbefine.member;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberUpdateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member creator;
    private Member user1;
    private Member user2;
    private String creatorAuthHeader;
    private String user1AuthHeader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        creator = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "creator",
                        "creator@naver.com",
                        Role.USER,
                        new OauthId(1L, KAKAO)
                )
        );
        user1 = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "user1",
                        "user1@naver.com",
                        Role.USER,
                        new OauthId(2L, KAKAO)
                )
        );
        user2 = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "user2",
                        "user2@naver.com",
                        Role.USER,
                        new OauthId(3L, KAKAO)
                )
        );
        creatorAuthHeader = testAuthHeaderProvider.createAuthHeader(creator);
        user1AuthHeader = testAuthHeaderProvider.createAuthHeader(user1);
    }

    @Test
    @DisplayName("회원 목록을 조회한다.")
    void findAllMember() {
        // given, when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, user1AuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members")
                .then().log().all()
                .extract();

        List<MemberResponse> memberResponses = response.as(new TypeRef<>() {
        });

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponses).hasSize(3)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        MemberResponse.from(creator),
                        MemberResponse.from(user1),
                        MemberResponse.from(user2))
                );
    }

    @Test
    @DisplayName("로그인 회원의 상세 정보를 단일 조회한다.")
    void findMyProfile() {
        // given, when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, user1AuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/my/profiles")
                .then().log().all()
                .extract();

        MemberDetailResponse memberDetailResponse = response.as(MemberDetailResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberDetailResponse)
                .usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(MemberDetailResponse.from(user1));
    }

    @Test
    @DisplayName("로그인 회원이 내 지도 목록을 조회하면, 200을 반환한다.")
    void findMyAllTopics_Success() {
        //when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/my/topics")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("로그인 회원이 내 지도 목록을 조회하면, 200을 반환한다.")
    void findMyAllPins_Success() {
        //when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/my/pins")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test

    @DisplayName("회원의 즐겨찾기 토픽 목록을 조회하면, 200을 반환한다.")
    void findTopicsInBookmarks_Success() {
        //when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/my/bookmarks")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("모아보기의 지도 목록 조회 시 200을 반환한다")
    void findTopicsFromAtlas_Success() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, creatorAuthHeader)
                .when().get("/members/my/atlas")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("회원 정보를 정상적으로 수정하면, 200을 반환한다")
    void updateMemberInfo_Success() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, creatorAuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberUpdateRequest("new nickname"))
                .when().patch("/members/my/profiles")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
