package com.mapbefine.mapbefine.topic.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.image.FileFixture;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.application.TopicCommandService;
import com.mapbefine.mapbefine.topic.application.TopicQueryService;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequestWithoutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequestWithoutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.ClusterResponse;
import com.mapbefine.mapbefine.topic.dto.response.RenderPinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class TopicControllerTest extends RestDocsIntegration {

    private static final List<TopicResponse> RESPONSES = List.of(new TopicResponse(
            1L,
            "준팍의 또 토픽",
            "https://map-befine-official.github.io/favicon.png",
            "준팍",
            3,
            Boolean.FALSE,
            5,
            Boolean.FALSE,
            LocalDateTime.now()
    ), new TopicResponse(
            2L,
            "준팍의 두번째 토픽",
            "https://map-befine-official.github.io/favicon.png",
            "준팍",
            5,
            Boolean.FALSE,
            3,
            Boolean.FALSE,
            LocalDateTime.now()
    ));

    @MockBean
    private TopicCommandService topicCommandService;
    @MockBean
    private TopicQueryService topicQueryService;

    @Test
    @DisplayName("토픽 새로 생성")
    void create() throws Exception {
        given(topicCommandService.saveTopic(any(), any())).willReturn(1L);
        TopicCreateRequestWithoutImage request = new TopicCreateRequestWithoutImage(
                "준팍의 안갈집",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                List.of(1L, 2L, 3L)
        );
        MockMultipartFile requestJson = new MockMultipartFile("request", "request", APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile("image", "test.png", IMAGE_PNG_VALUE, "data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(POST, "/topics/new")
                        .file(image)
                        .file(requestJson)
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 병합 생성")
    void mergeAndCreate() throws Exception {
        given(topicCommandService.merge(any(), any())).willReturn(1L);

        TopicMergeRequestWithoutImage request = new TopicMergeRequestWithoutImage(
                "준팍의 안갈집",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                List.of(1L, 2L, 3L)
        );
        MockMultipartFile requestJson = new MockMultipartFile("request", "request", APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile("image", "test.png", IMAGE_PNG_VALUE, "data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(POST, "/topics/merge")
                        .file(image)
                        .file(requestJson)
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀을 권한이 있는 토픽에 복사할 수 있다.")
    void copyPin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/topics/1/copy?pinIds=1,2,3")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 수정")
    void update() throws Exception {
        TopicUpdateRequest topicUpdateRequest = new TopicUpdateRequest(
                "준팍의 안갈집",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 이미지 수정")
    void updateImage() throws Exception {
        MockMultipartFile image = FileFixture.createFile();

        mockMvc.perform(MockMvcRequestBuilders.multipart(PUT, "/topics/images/1")
                        .file(image)
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 삭제")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 목록 조회")
    void findAll() throws Exception {
        given(topicQueryService.findAllReadable(any())).willReturn(RESPONSES);

        mockMvc.perform(MockMvcRequestBuilders.get("/topics")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 상세 조회")
    void findById() throws Exception {
        TopicDetailResponse topicDetailResponse = new TopicDetailResponse(
                1L,
                "준팍의 두번째 토픽",
                "준팍이 막 만든 두번째 토픽",
                "https://map-befine-official.github.io/favicon.png",
                "준팍",
                2,
                Boolean.FALSE,
                0,
                Boolean.FALSE,
                Boolean.FALSE,
                LocalDateTime.now(),
                List.of(
                        new PinResponse(
                                1L,
                                "매튜의 산스장",
                                "지번 주소",
                                "매튜가 사랑하는 산스장",
                                "매튜",
                                37,
                                127
                        ), new PinResponse(
                                2L,
                                "매튜의 안갈집",
                                "지번 주소",
                                "매튜가 두번은 안 갈 집",
                                "매튜",
                                37,
                                127
                        )
                )
        );
        given(topicQueryService.findDetailById(any(), any())).willReturn(topicDetailResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 수정일 기준으로 토픽을 나열한다")
    void findAllByOrderByUpdatedAtDesc() throws Exception {
        given(topicQueryService.findAllByOrderByUpdatedAtDesc(any())).willReturn(RESPONSES);

        mockMvc.perform(MockMvcRequestBuilders.get("/topics/newest")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원 Id를 입력하면 해당 회원이 만든 지도 목록을 조회할 수 있다.")
    void findAllTopicsByMemberId() throws Exception {
        given(topicQueryService.findAllTopicsByMemberId(any(), any())).willReturn(RESPONSES);

        mockMvc.perform(MockMvcRequestBuilders.get("/topics/members?id=1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("인기 토픽을 조회할 수 있다.")
    void findAllBestTopics() throws Exception {
        given(topicQueryService.findAllBestTopics(any())).willReturn(RESPONSES);

        mockMvc.perform(MockMvcRequestBuilders.get("/topics/bests")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("클러스터링 된 핀들을 조회할 수 있다.")
    void getClustersOfPins() throws Exception {
        given(topicQueryService.findClustersPinsByIds(any(), any(), anyDouble())).willReturn(
                List.of(
                    new ClusterResponse(
                            36.0,
                            124.0,
                            List.of(
                                    new RenderPinResponse(1L, "firstTopic의 핀", 1L),
                                    new RenderPinResponse(2L, "secondTopic의 핀", 2L)
                            )
                    ),
                    new ClusterResponse(
                            36.0,
                            124.2,
                            List.of(
                                    new RenderPinResponse(3L, "firstTopic의 핀", 1L)
                            )
                    ),
                    new ClusterResponse(
                            38.0,
                            124.0,
                            List.of(
                                    new RenderPinResponse(4L, "firstTopic의 핀", 1L),
                                    new RenderPinResponse(5L, "secondTopic의 핀", 2L)
                            )
                    )
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/topics/clusters?ids=1,2&image-diameter=9000")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

}
