package com.mapbefine.mapbefine.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.MemberFixture;
import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.member.Role;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;
import com.mapbefine.mapbefine.repository.MemberRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TopicIntegrationTest extends IntegrationTest {

    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Pin 목록 없이 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithoutPins_Success() {
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest(
                "준팍의 또간집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                Collections.emptyList()
        );

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집, member);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> createNewTopic(TopicCreateRequest request, Member member) {
        return RestAssured.given()
                .log().all()
                .header(AUTHORIZATION, member.getEmail())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/topics/new")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Pin 목록과 함께 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithPins_Success() {
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        List<Pin> pins = pinRepository.findAll();
        List<Long> pinIds = pins.stream()
                .map(Pin::getId)
                .toList();

        TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest(
                "준팍의 또간집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 2번 이상 간집 ",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                pinIds
        );

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집, member);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("여러개의 토픽을 병합하면 201을 반환한다")
    void createMergeTopic_Success() {
        // given
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        // TODO : 준팍! 해당 Merge Test 에는 topicRepository.findAll 을 통해서 이미 저장되어 있던 토픽들을 병합해주는 것으로 보이는데
        // TODO : 이러면 다른 테스트에 의존적이게 되지 않을까요? 일단은 의도가 있었을 수도 있을까봐 데이터를 추가하는 로직은 주석처리 해놓겠습니다.
//        TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest(
//                "준팍의 또간집",
//                "https://map-befine-official.github.io/favicon.png",
//                "준팍이 2번 이상 간집 ",
//                Publicity.PUBLIC,
//                Permission.ALL_MEMBERS,
//                Collections.emptyList()
//        );
//        TopicCreateRequest 준팍의_또안간집 = new TopicCreateRequest(
//                "준팍의 또안간집",
//                "https://map-befine-official.github.io/favicon.png",
//                "준팍이 2번 이상 안간집 ",
//                Publicity.PUBLIC,
//                Permission.ALL_MEMBERS,
//                Collections.emptyList()
//        );
//        createNewTopic(준팍의_또간집, member);
//        createNewTopic(준팍의_또안간집, member);
        List<Topic> topics = topicRepository.findAll();
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        TopicMergeRequest 송파_데이트코스 = new TopicMergeRequest(
                "송파 데이트코스",
                "https://map-befine-official.github.io/favicon.png",
                "맛집과 카페 토픽 합치기",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                topicIds);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, member.getEmail())
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
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        ExtractableResponse<Response> newTopic = createNewTopic(
                new TopicCreateRequest(
                        "준팍의 또간집",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍이 두번 간집",
                        Publicity.PUBLIC,
                        Permission.ALL_MEMBERS,
                        Collections.emptyList()
                ),
                member
        );
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        TopicUpdateRequest 송파_데이트코스 = new TopicUpdateRequest(
                "송파 데이트코스",
                "https://map-befine-official.github.io/favicon.png",
                "수정한 토픽"
        );
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, member.getEmail())
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
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        ExtractableResponse<Response> newTopic = createNewTopic(
                new TopicCreateRequest(
                        "준팍의 또간집",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍이 두번 간집 ",
                        Publicity.PUBLIC,
                        Permission.ALL_MEMBERS,
                        Collections.emptyList()
                ),
                member
        );
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(AUTHORIZATION, member.getEmail())
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
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        TopicCreateRequest request = new TopicCreateRequest(
                "topicName",
                "image",
                "description",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                Collections.emptyList()
        );
        ExtractableResponse<Response> createResponse = createNewTopic(request, member);
        String locationHeader = createResponse.header("Location");
        long topicId = Long.parseLong(locationHeader.split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/topics/{id}", topicId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
