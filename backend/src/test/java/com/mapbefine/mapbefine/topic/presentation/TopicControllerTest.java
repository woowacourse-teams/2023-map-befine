package com.mapbefine.mapbefine.topic.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.application.TopicCommandService;
import com.mapbefine.mapbefine.topic.application.TopicQueryService;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class TopicControllerTest extends RestDocsIntegration { // TODO: 2023/07/25 Image 칼람 추가됨으로 인해 수정 필요

    private static final String BASIC_FORMAT = "Basic %s";

    @MockBean
    private TopicCommandService topicCommandService;

    @MockBean
    private TopicQueryService topicQueryService;

    private static final Member member =
            MemberFixture.create("member", "member@naver.com", Role.USER);

    @Test
    @DisplayName("토픽 새로 생성")
    void create() throws Exception {
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        given(topicCommandService.saveTopic(any(), any())).willReturn(1L);

        TopicCreateRequest topicCreateRequest = new TopicCreateRequest(
                "준팍의 안갈집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                List.of(1L, 2L, 3L)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/new")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicCreateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 병합 생성")
    void mergeAndCreate() throws Exception {
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        given(topicCommandService.merge(any(), any())).willReturn(1L);
        TopicMergeRequest topicMergeRequest = new TopicMergeRequest(
                "준팍의 안갈집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                List.of(1L, 2L, 3L)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/topics/merge")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicMergeRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 수정")
    void update() throws Exception {
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        TopicUpdateRequest topicUpdateRequest = new TopicUpdateRequest(
                "준팍의 안갈집",
                "https://map-befine-official.github.io/favicon.png",
                "준팍이 두번 다시 안갈집",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/topics/1")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicUpdateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 삭제")
    void delete() throws Exception {
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/topics/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 목록 조회")
    void findAll() throws Exception {
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        List<TopicResponse> responses = List.of(new TopicResponse(
                1L,
                "준팍의 또 토픽",
                "https://map-befine-official.github.io/favicon.png",
                3,
                false,
                LocalDateTime.now()
        ), new TopicResponse(
                2L,
                "준팍의 두번째 토픽",
                "https://map-befine-official.github.io/favicon.png",
                5,
                false,
                LocalDateTime.now()
        ));
        given(topicQueryService.findAllReadable(any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/topics")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("토픽 상세 조회")
    void findById() throws Exception {
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        TopicDetailResponse topicDetailResponse = new TopicDetailResponse(
                1L,
                "준팍의 두번째 토픽",
                "준팍이 막 만든 두번째 토픽",
                "https://map-befine-official.github.io/favicon.png",
                2,
                false,
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
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
