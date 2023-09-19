package com.mapbefine.mapbefine.topic;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

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
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequestWithOutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import io.restassured.*;
import io.restassured.response.*;
import java.io.File;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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

    @BeforeEach
    void setMember() {
        member = memberRepository.save(MemberFixture.create("other", "other@othter.com", Role.ADMIN));
        topic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        location = locationRepository.save(LocationFixture.create());
        authHeader = testAuthHeaderProvider.createAuthHeader(member);
    }


    @Test
    @DisplayName("Pin 목록 없이 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithoutPins_Success() {
        TopicCreateRequestWithOutImage 준팍의_또간집 = new TopicCreateRequestWithOutImage(
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

    private ExtractableResponse<Response> createNewTopic(TopicCreateRequestWithOutImage request, String authHeader) {
        String imageFilePath = getClass().getClassLoader()
                .getResource("test.png")
                .getPath();
        File mockFile = new File(imageFilePath);
        
//        MockMultipartFile mockFile = new MockMultipartFile( // 이것은 왜 그런 것일까??
//                "test",
//                "test.png",
//                "image/png",
//                "byteCode".getBytes()
//        );

        return RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, authHeader)
                .multiPart("image", mockFile, MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("request", request, MediaType.APPLICATION_JSON_VALUE)
                .when().post("/topics/new")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Pin 목록과 함께 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithPins_Success() {

        PinFixture.create(location, topic, member);

        List<Pin> pins = pinRepository.findAll();
        List<Long> pinIds = pins.stream()
                .map(Pin::getId)
                .toList();

        TopicCreateRequestWithOutImage 준팍의_또간집 = new TopicCreateRequestWithOutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                pinIds
        );

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집, authHeader);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("여러개의 토픽을 병합하면 201을 반환한다")
    void createMergeTopic_Success() {
        // given
        TopicCreateRequestWithOutImage 준팍의_또간집 = new TopicCreateRequestWithOutImage(
                "준팍의 또간집",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                Collections.emptyList()
        );
        TopicCreateRequestWithOutImage 준팍의_또안간집 = new TopicCreateRequestWithOutImage(
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

        TopicMergeRequest 송파_데이트코스 = new TopicMergeRequest(
                "송파 데이트코스",
                "https://map-befine-official.github.io/favicon.png",
                "맛집과 카페 토픽 합치기",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                topicIds);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(송파_데이트코스)
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
                new TopicCreateRequestWithOutImage(
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
                "https://map-befine-official.github.io/favicon.png",
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
    @DisplayName("Topic을 삭제하면 204를 반환한다")
    void deleteTopic_Success() {
        ExtractableResponse<Response> newTopic = createNewTopic(
                new TopicCreateRequestWithOutImage(
                        "준팍의 또간집",
                        "준팍이 두번 간집 ",
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/topics/{id}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
        TopicCreateRequestWithOutImage request = new TopicCreateRequestWithOutImage(
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
        TopicCreateRequestWithOutImage request = new TopicCreateRequestWithOutImage(
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

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bestOneTopic, member);
        bookmarkRepository.save(bookmark);

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

}
