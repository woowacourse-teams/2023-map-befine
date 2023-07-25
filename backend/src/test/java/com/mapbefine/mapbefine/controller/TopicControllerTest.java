package com.mapbefine.mapbefine.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicDetailResponse;
import com.mapbefine.mapbefine.dto.TopicFindBestRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicResponse;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.service.TopicCommandService;
import com.mapbefine.mapbefine.service.TopicQueryService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class TopicControllerTest extends RestDocsIntegration { // TODO: 2023/07/25 Image 칼람 추가됨으로 인해 수정 필요

    @MockBean
    private TopicCommandService topicCommandService;

    @MockBean
    private TopicQueryService topicQueryService;


    @Test
    @DisplayName("토픽 새로 생성")
    void create() throws Exception {
        given(topicCommandService.createNew(any())).willReturn(1L);
        TopicCreateRequest topicCreateRequest = new TopicCreateRequest("준팍의 안갈집", "https://map-befine-official.github.io/favicon.png", "준팍이 두번 다시 안갈집", List.of(1L, 2L, 3L));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicCreateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 병합 생성")
    void mergeAndCreate() throws Exception {
        given(topicCommandService.createMerge(any())).willReturn(1L);
        TopicMergeRequest topicMergeRequest = new TopicMergeRequest("준팍의 안갈집", "https://map-befine-official.github.io/favicon.png", "준팍이 두번 다시 안갈집", List.of(1L, 2L, 3L));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/merge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicMergeRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 수정")
    void update() throws Exception {
        TopicUpdateRequest topicUpdateRequest = new TopicUpdateRequest("준팍의 안갈집", "준팍이 두번 다시 안갈집");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/topics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicUpdateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 삭제")
    void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/topics/1")
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 목록 조회")
    void findAll() throws Exception {
        List<TopicResponse> responses = List.of(new TopicResponse(
                1L,
                "준팍의 또 토픽",
                "https://map-befine-official.github.io/favicon.png",
                3,
                LocalDateTime.now()
        ), new TopicResponse(
                2L,
                "준팍의 두번째 토픽",
                "https://map-befine-official.github.io/favicon.png",
                5,
                LocalDateTime.now()
        ));
        given(topicQueryService.findAll()).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics")
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 상세 조회")
    void findById() throws Exception {
        TopicDetailResponse topicDetailResponse = new TopicDetailResponse(
                1L,
                "준팍의 두번째 토픽",
                "준팍이 막 만든 두번째 토픽",
                "https://map-befine-official.github.io/favicon.png",
                2,
                LocalDateTime.now(),
                List.of(
                        new PinResponse(
                                1L,
                                "매튜의 산스장",
                                "지번 주소",
                                "매튜가 사랑하는 산스장",
                                new BigDecimal(37).toString(),
                                new BigDecimal(127).toString()
                        ), new PinResponse(
                                2L,
                                "매튜의 안갈집",
                                "지번 주소",
                                "매튜가 두번은 안 갈 집",
                                new BigDecimal(37).toString(),
                                new BigDecimal(127).toString()
                        )
                )
        );
        given(topicQueryService.findById(any())).willReturn(topicDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics/1")
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("현재 위치를 기준 토픽의 핀 개수로 나열한다.")
    void findBests() throws Exception {
        List<TopicResponse> responses = List.of(new TopicResponse(
                1L,
                "준팍의 또 토픽",
                "https://map-befine-official.github.io/favicon.png",
                5,
                LocalDateTime.now()
        ), new TopicResponse(
                2L,
                "준팍의 두번째 토픽",
                "https://map-befine-official.github.io/favicon.png",
                3,
                LocalDateTime.now()
        ));
        TopicFindBestRequest topicFindBestRequest = new TopicFindBestRequest("37", "127");
        given(topicQueryService.findBests(any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics/best")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicFindBestRequest))
        ).andDo(restDocs.document());
    }
}
