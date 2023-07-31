package com.mapbefine.mapbefine.pin;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.*;
import io.restassured.response.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class PinIntegrationTest extends IntegrationTest {

    private static final String AUTHORIZATION = "Authorization";
    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

    private Topic topic;
    private Location location;
    private Member member;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    void saveTopicAndLocation() {
        member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        topic = topicRepository.save(TopicFixture.createByName("PinIntegration 토픽", member));
        location = locationRepository.save(LocationFixture.createByCoordinate(37.5152933, 127.1029866));
    }

    @Test
    @DisplayName("핀을 생성하면 저장된 Pin의 Location과 201을 반환한다.")
    void addIfExistDuplicateLocation_Success() {
        //given
        Address address = location.getAddress();
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "pin",
                "description",
                address.getRoadBaseAddress(),
                "legalDongCode",
                new BigDecimal("37.5152933"),
                new BigDecimal("127.1029866"),
                BASE_IMAGES
        );

        //when
        ExtractableResponse<Response> response = createPin(request);

        //then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> createPin(PinCreateRequest request) {
        return RestAssured.given().log().all()
                .header(AUTHORIZATION, member.getEmail())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/pins")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("핀을 생성하면 저장된 Pin의 Location과 201을 반환한다.")
    void addIfNotExistDuplicateLocation_Success() {
        //given
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "pin",
                "description",
                "기존에 없는 주소",
                "legalDongCode",
                new BigDecimal("37"),
                new BigDecimal("126"),
                BASE_IMAGES
        );

        //when
        ExtractableResponse<Response> response = createPin(request);

        //then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("핀 목록을 조회하면 저장된 Pin의 목록과 200을 반환한다.")
    void findAll_Success() {
        // given
        PinCreateRequest request1 = new PinCreateRequest(
                topic.getId(),
                "pin1",
                "description",
                "기존에 없는 주소",
                "legalDongCode",
                new BigDecimal("37"),
                new BigDecimal("126"),
                BASE_IMAGES
        );

        PinCreateRequest request2 = new PinCreateRequest(
                topic.getId(),
                "pine2",
                "description",
                "기존에 없는 주소",
                "legalDongCode",
                new BigDecimal("37.12345"),
                new BigDecimal("126.12345"),
                BASE_IMAGES
        );

        createPin(request1);
        createPin(request2);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/pins")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name"))
                .contains(request1.name(), request2.name());
        assertThat(response.body().jsonPath().getList("description"))
                .contains(request1.description(), request2.description());
    }

    @Test
    @DisplayName("핀 상세 조회를 하면 Pin 정보와 함께 200을 반환한다.")
    void findDetail_Success() {
        // given
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "pin",
                "description",
                "기존에 없는 주소",
                "legalDongCode",
                new BigDecimal("37"),
                new BigDecimal("126"),
                BASE_IMAGES
        );
        ExtractableResponse<Response> createResponse = createPin(request);
        String locationHeader = createResponse.header("Location");
        long pinId = Long.parseLong(locationHeader.split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/pins/" + pinId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo(request.name());
        assertThat(response.jsonPath().getString("description")).isEqualTo(request.description());
        assertThat(response.jsonPath().getString("address")).isEqualTo(request.address());
    }

}
