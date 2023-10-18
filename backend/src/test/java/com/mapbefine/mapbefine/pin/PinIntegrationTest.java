package com.mapbefine.mapbefine.pin;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import com.mapbefine.mapbefine.pin.domain.PinCommentRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCommentCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinCommentUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinCommentResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class PinIntegrationTest extends IntegrationTest {

    private Topic topic;
    private Location location;
    private Member member;
    private String authHeader;

    private PinCreateRequest createRequestDuplicateLocation;
    private PinCreateRequest createRequestNoDuplicateLocation;
    private PinCreateRequest createRequestNoDuplicateLocation2;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PinCommentRepository pinCommentRepository;

    @Autowired
    private PinRepository pinRepository;

    @BeforeEach
    void saveTopicAndLocation() {
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        authHeader = testAuthHeaderProvider.createAuthHeader(member);
        topic = topicRepository.save(TopicFixture.createByName("PinIntegration 토픽", member));
        location = locationRepository.save(LocationFixture.createByCoordinate(37.5152933, 127.1029866));

        createRequestDuplicateLocation = new PinCreateRequest(
                topic.getId(),
                "pin",
                "description",
                location.getAddress()
                        .getRoadBaseAddress(),
                "legalDongCode",
                location.getLatitude(),
                location.getLongitude()
        );
        createRequestNoDuplicateLocation = new PinCreateRequest(
                topic.getId(),
                "pin",
                "description",
                "address",
                "legalDongCode",
                37,
                126
        );
        createRequestNoDuplicateLocation2 = new PinCreateRequest(
                topic.getId(),
                "pine2",
                "description",
                "address",
                "legalDongCode",
                37.12345,
                126.12345
        );
    }

    @Test
    @DisplayName("Pin을 생성하면 저장된 Pin의 Location 헤더값과 201을 반환한다.")
    void addIfExistDuplicateLocation_Success() {
        //given, when
        ExtractableResponse<Response> response = createPin(createRequestDuplicateLocation);

        //then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> createPin(PinCreateRequest request) {
        String imageFilePath = getClass().getClassLoader()
                .getResource("test.png")
                .getPath();

        return RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("images", new File(imageFilePath), MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("images", new File(imageFilePath), MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("images", new File(imageFilePath), MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("request", request, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/pins")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Image List 없이 Pin 을 정상적으로 생성한다.")
    void addIfNonExistImageList_Success() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("request", createRequestDuplicateLocation, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/pins")
                .then().log().all()
                .extract();

        // then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Pin을 생성하면 저장된 Pin의 Location 헤더값과 201을 반환한다.")
    void addIfNotExistDuplicateLocation_Success() {
        //given, when
        ExtractableResponse<Response> response = createPin(createRequestNoDuplicateLocation);

        //then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Pin 목록을 조회하면 저장된 Pin의 목록과 200을 반환한다.")
    void findAll_Success() {
        // given
        createPin(createRequestNoDuplicateLocation);
        createPin(createRequestNoDuplicateLocation2);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/pins")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name"))
                .contains(createRequestNoDuplicateLocation.name(),
                        createRequestNoDuplicateLocation2.name());
        assertThat(response.body().jsonPath().getList("description"))
                .contains(createRequestNoDuplicateLocation.description(),
                        createRequestNoDuplicateLocation2.description());
    }

    @Test
    @DisplayName("Pin 상세 조회를 하면 Pin 정보와 함께 200을 반환한다.")
    void findDetail_Success() {
        // given
        long pinId = createPinAndGetId(createRequestNoDuplicateLocation);

        // when
        ExtractableResponse<Response> response = findById(pinId);

        PinDetailResponse as = response.as(PinDetailResponse.class);

        // then
        assertThat(response.jsonPath().getString("name"))
                .isEqualTo(createRequestNoDuplicateLocation.name());
        assertThat(response.jsonPath().getString("description"))
                .isEqualTo(createRequestNoDuplicateLocation.description());
        assertThat(response.jsonPath().getString("address"))
                .isEqualTo(createRequestNoDuplicateLocation.address());
    }

    private ExtractableResponse<Response> findById(long pinId) {
        return RestAssured.given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/pins/" + pinId)
                .then().log().all()
                .extract();
    }

    private long createPinAndGetId(PinCreateRequest request) {
        ExtractableResponse<Response> createResponse = createPin(request);
        String locationHeader = createResponse.header("Location");
        return Long.parseLong(locationHeader.split("/")[2]);
    }

    @Test
    @DisplayName("특정 Pin 에 대한 PinImage 를 생성하면(이미지 추가) 201을 반환한다.")
    void addImage_Success() {
        // given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);

        // when
        ExtractableResponse<Response> response = createPinImage(pinId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> createPinImage(long pinId) {
        String imageFilePath = getClass().getClassLoader()
                .getResource("test.png")
                .getPath();
        File mockFile = new File(imageFilePath);

        return RestAssured.given().log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("image", mockFile, MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("pinId", pinId, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/pins/images")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("특정 PinImage 를 삭제하면 204를 반환한다.")
    void removeImage_Success() {
        // given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);
        createPinImage(pinId);
        ExtractableResponse<Response> pinResponse = findById(pinId);
        List<PinImageResponse> images = pinResponse.jsonPath().getList("images", PinImageResponse.class);
        PinImageResponse deleteImage = images.get(0);

        // when
        long pinImageId = deleteImage.id();
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/pins/images/" + pinImageId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @Test
    @DisplayName("회원별 Pin 목록을 조회하면 200을 반환한다")
    void findAllPinsByMemberId_Success() {
        //given
        createPinAndGetId(createRequestDuplicateLocation);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("id", member.getId())
                .when().get("/pins/members")
                .then().log().all()
                .extract();

        List<PinResponse> pinResponses = response.jsonPath().getList(".", PinResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(pinResponses).hasSize(1);
    }

    @Test
    @DisplayName("핀 댓글을 조회하면 200을 반환한다.")
    void findPinCommentPinId_Success() {
        //given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);

        Pin pin = pinRepository.findById(pinId).get();
        PinComment parentPinComment = pinCommentRepository.save(
                PinCommentFixture.createParentComment(pin, member)
        );
        PinComment childPinComment = pinCommentRepository.save(
                PinCommentFixture.createChildComment(pin, member, parentPinComment)
        );
        List<PinCommentResponse> expected = List.of(
                PinCommentResponse.of(parentPinComment, true),
                PinCommentResponse.of(childPinComment, true)
        );

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/pins/comments/" + pinId)
                .then().log().all()
                .extract();

        List<PinCommentResponse> pinResponses = response.as(new TypeRef<>() {});

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(pinResponses).hasSize(2);
        assertThat(pinResponses).usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀 댓글을 생성하면 201 을 반환한다.")
    void addParentPinComment_Success() {
        //given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);
        PinCommentCreateRequest request = new PinCommentCreateRequest(
                pinId,
                null,
                "댓글"
        );

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/pins/comments")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("핀 대댓글을 생성하면 201 을 반환한다.")
    void addChildPinComment_Success() {
        //given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);
        Long parentPinCommentId = createParentPinComment(pinId);
        PinCommentCreateRequest childPinCommentRequest = new PinCommentCreateRequest(
                pinId,
                parentPinCommentId,
                "대댓글"
        );

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(childPinCommentRequest)
                .when().post("/pins/comments")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long createParentPinComment(long pinId) {
        PinCommentCreateRequest parentPinCommentRequest = new PinCommentCreateRequest(
                pinId,
                null,
                "댓글"
        );

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(parentPinCommentRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/pins/comments")
                .then().log().all()
                .extract();

        String locationHeader = createResponse.header("Location");
        return Long.parseLong(locationHeader.split("/")[3]);
    }

    @Test
    @DisplayName("핀 댓글을 수정하면 201 을 반환한다.")
    void updatePinComment_Success() {
        //given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);
        PinCommentUpdateRequest request = new PinCommentUpdateRequest(
                "댓그으으을"
        );
        long pinCommentId = createParentPinComment(pinId);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/pins/comments/" + pinCommentId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("핀 댓글을 삭제하면 204 을 반환한다.")
    void removePinComment_Success() {
        //given
        long pinId = createPinAndGetId(createRequestDuplicateLocation);
        Long parentPinCommentId = createParentPinComment(pinId);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/pins/comments/" + parentPinCommentId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}

