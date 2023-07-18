package com.mapbefine.mapbefine.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.repository.PinRepository;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TopicIntegrationTest extends IntegrationTest {

	@Autowired
	private PinRepository pinRepository;

	@Test
	@DisplayName("Pin 목록 없이 Topic을 생성하면 201을 반환한다")
	void createNewTopicWithoutPins_Success() {
		TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest("준팍의 또간집", "준팍이 2번 이상 간집 ", Collections.emptyList());

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
		TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest(
			"준팍의 또간집",
			"준팍이 2번 이상 간집 ",
			List.of(1L, 2L));

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
		TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest("준팍의 또간집", "준팍이 2번 이상 간집 ", List.of(1L));
		TopicCreateRequest 도이의_또간집 = new TopicCreateRequest("도이의 또간집", "도이가 2번 이상 간집 ", List.of(2L));
		createNewTopic(준팍의_또간집);
		createNewTopic(도이의_또간집);

		TopicMergeRequest 송파_데이트코스 = new TopicMergeRequest("송파 데이트코스", "맛집과 카페 토픽 합치기", List.of(1L, 2L));

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
		TopicCreateRequest 준팍의_또간집 = new TopicCreateRequest("준팍의 또간집", "준팍이 2번 이상 간집 ", List.of(1L));
		final ExtractableResponse<Response> createResponse = createNewTopic(준팍의_또간집);
		long topicId = Long.parseLong(createResponse.header("Location").split("/")[2]);

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
		assertThat(response.header("Location")).isNull();
	}

	@Test
	@DisplayName("Topic을 삭제하면 204를 반환한다")
	void deleteTopic_Success() {
		final TopicCreateRequest 송파_데이트코스 = new TopicCreateRequest("송파 데이트코스", "맛집과 카페 토픽 합치기", List.of(1L, 2L));
		final ExtractableResponse<Response> createResponse = createNewTopic(송파_데이트코스);
		long topicId = Long.parseLong(createResponse.header("Location").split("/")[2]);

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(송파_데이트코스)
			.when().delete("/topics/{id}", topicId)
			.then().log().all()
			.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(response.header("Location")).isNull();
	}
}
