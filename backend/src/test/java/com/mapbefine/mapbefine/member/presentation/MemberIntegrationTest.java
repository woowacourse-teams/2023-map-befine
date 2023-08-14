package com.mapbefine.mapbefine.member.presentation;

import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static io.restassured.RestAssured.*;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import io.restassured.common.mapper.*;
import io.restassured.response.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

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
    @DisplayName("유저 목록을 조회한다.")
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
    @DisplayName("유저를 단일 조회한다.")
    void findMemberById() {
        // given, when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, user1AuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/" + user1.getId())
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
    @DisplayName("유저가 생성한 핀을 조회한다.")
    void findPinsByMember() {
        // given
        Location location = locationRepository.save(LocationFixture.create());
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        Pin pin1 = pinRepository.save(PinFixture.create(location, topic, creator));
        Pin pin2 = pinRepository.save(PinFixture.create(location, topic, creator));

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/pins")
                .then().log().all()
                .extract();
        List<PinResponse> pinResponses = response.as(new TypeRef<>() {
        });

        // then
        assertThat(pinResponses).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(PinResponse.from(pin1), PinResponse.from(pin2)));
    }

    @Test
    @DisplayName("유저가 생성한 토픽을 조회한다.")
    void findTopicsByMember() {
        // given
        Topic topic1 = topicRepository.save(TopicFixture.createByName("topic1", creator));
        Topic topic2 = topicRepository.save(TopicFixture.createByName("topic2", creator));

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/topics")
                .then().log().all()
                .extract();
        List<TopicResponse> topicResponses = response.as(new TypeRef<>() {
        });

        // then
        assertThat(topicResponses).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(List.of(TopicResponse.from(topic1, false), TopicResponse.from(topic2, false)));
    }

}
