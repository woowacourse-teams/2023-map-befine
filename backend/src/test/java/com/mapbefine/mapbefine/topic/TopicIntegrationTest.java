package com.mapbefine.mapbefine.topic;

import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequestWithoutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequestWithoutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.auth.AUTH;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

class TopicIntegrationTest extends IntegrationTest {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private Member member;
    private Topic topic;
    private Location location;
    private String authHeader;
    private File mockFile;

    @BeforeEach
    void setMember() {
        member = memberRepository.save(MemberFixture.create("other", "other@othter.com", Role.ADMIN));
        topic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        location = locationRepository.save(LocationFixture.create());
        authHeader = testAuthHeaderProvider.createAuthHeader(member);
        mockFile = new File(
                getClass().getClassLoader()
                        .getResource("test.png")
                        .getPath()
        );

    }

    @Test
    @DisplayName("Pin 목록 없이 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithoutPins_Success() {
        TopicCreateRequestWithoutImage 준팍의_또간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집, authHeader);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> createNewTopic(TopicCreateRequestWithoutImage request, String authHeader) {
        return RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("image", mockFile, MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("request", request, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/topics/new")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createNewTopicExcludeImage(
            TopicCreateRequestWithoutImage request,
            String authHeader
    ) {
        return RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("request", request, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/topics/new")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Pin 목록과 함께 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithPins_Success() {
        Pin pin = pinRepository.save(PinFixture.create(location, topic, member));
        TopicCreateRequestWithoutImage 준팍의_또간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                List.of(pin.getId())
        );

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집, authHeader);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("Image 없이 Topic 을 생성하더라도 201을 반환한다")
    void createNewTopicNonExistsImage_Success() {
        PinFixture.create(location, topic, member);

        List<Pin> pins = pinRepository.findAll();
        List<Long> pinIds = pins.stream()
                .map(Pin::getId)
                .toList();

        TopicCreateRequestWithoutImage 준팍의_또간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                pinIds
        );

        // when
        ExtractableResponse<Response> response = createNewTopicExcludeImage(준팍의_또간집, authHeader);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("여러개의 토픽을 병합하면 201을 반환한다")
    void createMergeTopic_Success() {
        // given
        TopicCreateRequestWithoutImage 준팍의_또간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        TopicCreateRequestWithoutImage 준팍의_또안간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또안간집",
                "준팍이 2번 이상 안간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        createNewTopic(준팍의_또간집, authHeader);
        createNewTopic(준팍의_또안간집, authHeader);
        List<Topic> topics = topicRepository.findAll();
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        TopicMergeRequestWithoutImage 송파_데이트코스 = new TopicMergeRequestWithoutImage(
                "송파 데이트코스",
                "맛집과 카페 토픽 합치기",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                topicIds);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("image", mockFile, MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("request", 송파_데이트코스, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/topics/merge")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("여러개의 토픽을 토픽 이미지 없이 병합해도 201을 반환한다")
    void createMergeTopicWithoutImage_Success() {
        // given
        TopicCreateRequestWithoutImage 준팍의_또간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        TopicCreateRequestWithoutImage 준팍의_또안간집 = new TopicCreateRequestWithoutImage(
                "준팍의 또안간집",
                "준팍이 2번 이상 안간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        createNewTopic(준팍의_또간집, authHeader);
        createNewTopic(준팍의_또안간집, authHeader);
        List<Topic> topics = topicRepository.findAll();
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        TopicMergeRequestWithoutImage 송파_데이트코스 = new TopicMergeRequestWithoutImage(
                "송파 데이트코스",
                "맛집과 카페 토픽 합치기",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                topicIds);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("request", 송파_데이트코스, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/topics/merge")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("Topic을 수정하면 200을 반환한다")
    void updateTopic_Success() {
        ExtractableResponse<Response> newTopic = createNewTopic(
                new TopicCreateRequestWithoutImage(
                        "준팍의 또간집",
                        "준팍이 두번 간집",
                        Publicity.PUBLIC,
                        PermissionType.ALL_MEMBERS,
                        Collections.emptyList()
                ),
                authHeader
        );
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        TopicUpdateRequest 송파_데이트코스 = new TopicUpdateRequest(
                "송파 데이트코스",
                "수정한 토픽",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS
        );
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(송파_데이트코스)
                .when().put("/topics/{id}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Topic 목록을 조회하면 200을 반환한다")
    void findTopics_Success() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/topics")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Topic 상세 정보를 조회하면 200을 반환한다")
    void findTopicDetail_Success() {
        //given
        TopicCreateRequestWithoutImage request = new TopicCreateRequestWithoutImage(
                "topicName",
                "description",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        ExtractableResponse<Response> createResponse = createNewTopic(request, authHeader);
        String locationHeader = createResponse.header("Location");
        long topicId = Long.parseLong(locationHeader.split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/topics/{id}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Topic 상세 정보 여러개를 조회하면 200을 반환한다")
    void findTopicDetailsByIds_Success() {
        //given
        TopicCreateRequestWithoutImage request = new TopicCreateRequestWithoutImage(
                "topicName",
                "description",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        ExtractableResponse<Response> createResponse1 = createNewTopic(request, authHeader);
        ExtractableResponse<Response> createResponse2 = createNewTopic(request, authHeader);
        String locationHeader1 = createResponse1.header("Location");
        String locationHeader2 = createResponse2.header("Location");
        long topicId1 = Long.parseLong(locationHeader1.split("/")[2]);
        long topicId2 = Long.parseLong(locationHeader2.split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("ids", topicId1 + "," + topicId2)
                .when().get("/topics/ids")
                .then().log().all()
                .extract();

        List<TopicDetailResponse> responses = response.jsonPath().getList(".", TopicDetailResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("인기 토픽 목록을 조회하면 200을 반환한다")
    void findAllBestTopics_Success() {
        //given
        Topic bestOneTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(bestOneTopic);

        RestAssured.given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().post("/bookmarks/topics/" + bestOneTopic.getId())
                .then().log().all();

        bestOneTopic = topicRepository.findById(bestOneTopic.getId()).get();

        // when
        List<TopicResponse> expect = List.of(
                TopicResponse.from(bestOneTopic, Boolean.FALSE, Boolean.TRUE),
                TopicResponse.from(topic, Boolean.FALSE, Boolean.FALSE)
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/topics/bests")
                .then().log().all()
                .extract();

        List<TopicResponse> actual = response.jsonPath().getList(".", TopicResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expect);
        assertThat(actual).extractingResultOf("id")
                .containsExactly(bestOneTopic.getId(), topic.getId());
    }


    @Test
    @DisplayName("핀이 추가/수정된 일자 기준 내림차순으로 토픽 목록을 조회할 경우, 200을 반환한다")
    void findAllByOrderByUpdatedAtDesc_Success() {
        topicRepository.deleteAll();

        // given
        Topic topic1 = topicRepository.save(TopicFixture.createByName("topic1", member));
        Topic topic2 = topicRepository.save(TopicFixture.createByName("topic2", member));
        Topic topic3 = topicRepository.save(TopicFixture.createByName("topic3", member));

        List<Pin> pins = List.of(
                PinFixture.create(location, topic1, member),
                PinFixture.create(location, topic2, member),
                PinFixture.create(location, topic2, member),
                PinFixture.create(location, topic3, member),
                PinFixture.create(location, topic3, member),
                PinFixture.create(location, topic3, member)
        );

        pinRepository.saveAll(pins);
        topicRepository.saveAll(List.of(topic1, topic2, topic3));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/topics/newest")
                .then().log().all()
                .extract();

        // then
        List<TopicResponse> topicResponses = response.jsonPath().getList(".", TopicResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(topicResponses).hasSize(3);
        assertThat(topicResponses.get(0).pinCount()).isEqualTo(3);
        assertThat(topicResponses.get(2).pinCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원별 Topic 목록을 조회하면 200을 반환한다")
    void findAllTopicsByMemberId_Success() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("id", member.getId())
                .when().get("/topics/members")

                .then().log().all()
                .extract();

        List<TopicResponse> responses = response.jsonPath().getList(".", TopicResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responses).hasSize(1);
    }

    @Test
    @DisplayName("게스트일 때도 모든 토픽을 조회할 수 있다.")
    void findAllTopicsWhenGuest() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/topics")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Topic의 이미지를 변경하면 200을 반환한다")
    void updateTopicImage_Success() {
        ExtractableResponse<Response> newTopic = createNewTopic(
                new TopicCreateRequestWithoutImage(
                        "준팍의 또간집",
                        "준팍이 두번 간집",
                        Publicity.PUBLIC,
                        PermissionType.ALL_MEMBERS,
                        Collections.emptyList()
                ),
                authHeader
        );
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("image", mockFile)
                .when().put("/topics/images/{id}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Topic의 클러스터링 된 핀들을 조회할 때 200을 반환한다.")
    void getClustersOfPins() {
        ExtractableResponse<Response> newTopic = createNewTopic(
                new TopicCreateRequestWithoutImage(
                        "매튜의 헬스장",
                        "맛있는 음식들이 즐비한 헬스장!",
                        Publicity.PUBLIC,
                        PermissionType.ALL_MEMBERS,
                        Collections.emptyList()
                ),
                authHeader
        );
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .param("ids", List.of(topicId))
                .param("image-diameter", 1)
                .when().get("/topics/clusters")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
