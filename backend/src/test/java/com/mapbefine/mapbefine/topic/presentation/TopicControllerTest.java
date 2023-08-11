package com.mapbefine.mapbefine.topic.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.application.TopicCommandService;
import com.mapbefine.mapbefine.topic.application.TopicQueryService;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class TopicControllerTest extends RestDocsIntegration {

    public static final List<TopicResponse> RESPONSES = List.of(
            new TopicResponse(
                    1L,
                    "준팍의 또 토픽",
                    "https://map-befine-official.github.io/favicon.png",
                    3,
                    LocalDateTime.of(2023, Month.AUGUST, 11, 10, 10, 10)
            ), new TopicResponse(
                    2L,
                    "준팍의 두번째 토픽",
                    "https://map-befine-official.github.io/favicon.png",
                    5,
                    LocalDateTime.of(2023, Month.AUGUST, 10, 10, 10, 10)
            )
    );
    private static final String AUTH_HEADER = Base64.encodeBase64String("Basic member@naver.com".getBytes());

    @MockBean
    private TopicCommandService topicCommandService;

    @MockBean
    private TopicQueryService topicQueryService;

    @Test
    @DisplayName("토픽 새로 생성")
    void create() throws Exception {
        given(topicCommandService.saveTopic(any(), any())).willReturn(1L);

        TopicCreateRequest topicCreateRequest = new TopicCreateRequest(
                "준팍의 안갈집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                List.of(1L, 2L, 3L)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/new")
                        .header(AUTHORIZATION, AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicCreateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 병합 생성")
    void mergeAndCreate() throws Exception {

        given(topicCommandService.merge(any(), any())).willReturn(1L);

        TopicMergeRequest topicMergeRequest = new TopicMergeRequest(
                "준팍의 안갈집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                List.of(1L, 2L, 3L)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/merge")
                        .header(AUTHORIZATION, AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicMergeRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀을 권한이 있는 토픽에 복사할 수 있다.")
    void copyPin() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/1/copy?pinIds=1,2,3")
                        .header(AUTHORIZATION, AUTH_HEADER)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 수정")
    void update() throws Exception {

        TopicUpdateRequest topicUpdateRequest = new TopicUpdateRequest(
                "준팍의 안갈집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/topics/1")
                        .header(AUTHORIZATION, AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicUpdateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 삭제")
    void delete() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/topics/1")
                        .header(AUTHORIZATION, AUTH_HEADER)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 목록 조회")
    void findAll() throws Exception {
        given(topicQueryService.findAllReadable(any())).willReturn(RESPONSES);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics")
                        .header(AUTHORIZATION, AUTH_HEADER)
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
                                37,
                                127
                        ), new PinResponse(
                                2L,
                                "매튜의 안갈집",
                                "지번 주소",
                                "매튜가 두번은 안 갈 집",
                                37,
                                127
                        )
                )
        );
        given(topicQueryService.findDetailById(any(), any())).willReturn(topicDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics/1")
                        .header(AUTHORIZATION, AUTH_HEADER)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 수정일 기준으로 토픽을 나열한다")
    void findAllByOrderByUpdatedAtDesc() throws Exception {
        given(topicQueryService.findAllByOrderByUpdatedAtDesc(any())).willReturn(RESPONSES);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics/newest")
                        .header(AUTHORIZATION, AUTH_HEADER)
        ).andDo(restDocs.document());
    }
}
