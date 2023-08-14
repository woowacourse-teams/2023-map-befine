package com.mapbefine.mapbefine.atlas;

import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.*;
import io.restassured.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class AtlasIntegrationTest extends IntegrationTest {

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AtlasRepository atlasRepository;

    private Member member;
    private Topic topic;
    private String authHeader;

    @BeforeEach
    void setMember() {
        member = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "creator",
                        "creator@naver.com",
                        Role.USER,
                        new OauthId(1L, KAKAO)
                )
        );
        topic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        authHeader = testAuthHeaderProvider.createAuthHeader(member);
    }

    @Test
    @DisplayName("모아보기의 지도 목록 조회 시 200을 반환한다")
    void findTopicsFromAtlas_Success() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .when().get("/atlas")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("모아보기에 지도를 추가 시 201을 반환한다")
    void addTopicToAtlas_Success() {
        //given
        Long topicId = topic.getId();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .when().post("/atlas/{topicId}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @Test
    @DisplayName("모아보기에 추가되어있는 지도 삭제 시 204를 반환한다")
    void removeTopicFromAtlas_Success() {
        //given
        Long topicId = topic.getId();
        atlasRepository.save(Atlas.from(topic, member));

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/atlas/{topicId}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
