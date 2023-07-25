package com.mapbefine.mapbefine.integration;

import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicIntegrationTest extends IntegrationTest {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    @DisplayName("Pin 목록 없이 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithoutPins_Success() {
        TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest("준팍의 또간집", "https://map-befine-official.github.io/favicon.png", "준팍이 2번 이상 간집 ", Collections.emptyList());

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> createNewTopic(TopicCreateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/topics/new")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Pin 목록과 함께 Topic을 생성하면 201을 반환한다")
    void createNewTopicWithPins_Success() {

        List<Pin> pins = pinRepository.findAll();
        List<Long> pinIds = pins.stream()
                .map(Pin::getId)
                .collect(Collectors.toList());

        TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest(
                "준팍의 또간집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 2번 이상 간집 ",
                pinIds);

        // when
        ExtractableResponse<Response> response = createNewTopic(준팍의_또간집);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("여러개의 토픽을 병합하면 201을 반환한다")
    void createMergeTopic_Success() {
        // given
        List<Topic> topics = topicRepository.findAll();
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .collect(Collectors.toList());

        TopicMergeRequest 송파_데이트코스 = new TopicMergeRequest("송파 데이트코스", "https://map-befine-official.github.io/favicon.png", "맛집과 카페 토픽 합치기", topicIds);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
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
                new TopicCreateRequest("준팍의 또간집", "https://map-befine-official.github.io/favicon.png", "준팍이 두번 간집", Collections.emptyList()));
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        TopicUpdateRequest 송파_데이트코스 = new TopicUpdateRequest("송파 데이트코스", "수정한 토픽");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
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
                new TopicCreateRequest("준팍의 또간집", "https://map-befine-official.github.io/favicon.png", "준팍이 두번 간집 ", Collections.emptyList()));
        long topicId = Long.parseLong(newTopic.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
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
        TopicCreateRequest request = new TopicCreateRequest(
                "topicName",
                "image",
                "description",
                Collections.emptyList()
        );
        ExtractableResponse<Response> createResponse = createNewTopic(request);
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
