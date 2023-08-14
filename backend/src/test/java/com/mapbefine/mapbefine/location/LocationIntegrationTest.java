package com.mapbefine.mapbefine.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import io.restassured.*;
import io.restassured.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LocationIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private String authHeader;

    @BeforeEach
    void setMember() {
        member = memberRepository.save(MemberFixture.create("other", "other@othter.com", Role.ADMIN));
        authHeader = testAuthHeaderProvider.createAuthHeader(member);
    }

    @Test
    @DisplayName("현재 위치의 좌표를 보내주면 주변 Topic을 핀 갯수 순으로 정렬하여 반환한다.")
    void findNearbyTopicsSortedByPinCount_Success() {
        //given
        Coordinate baseCoordinate = LocationFixture.BASE_COORDINATE;

        //when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("latitude", baseCoordinate.getLatitude())
                .queryParam("longitude", baseCoordinate.getLongitude())
                .when().get("/locations/bests")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
