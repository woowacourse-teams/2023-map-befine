package com.mapbefine.mapbefine.atlas.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.atlas.application.AtlasQueryService;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class AtlasControllerTest extends RestDocsIntegration {

    @MockBean
    private AtlasQueryService atlasQueryService;

    private final String authHeader = Base64.encodeBase64String("Basic member@naver.com".getBytes());
    private final List<TopicResponse> responses = List.of(
            new TopicResponse(
                    1L,
                    "준팍의 또 토픽",
                    "https://map-befine-official.github.io/favicon.png",
                    5,
                    true,
                    LocalDateTime.now()
            ), new TopicResponse(
                    2L,
                    "준팍의 두번째 토픽",
                    "https://map-befine-official.github.io/favicon.png",
                    3,
                    true,
                    LocalDateTime.now()
            )
    );

    @Test
    @DisplayName("모아보기에 추가되어있는 지도 목록을 조회한다")
    void findTopicsFromAtlas_Success() throws Exception {
        // when
        given(atlasQueryService.findTopicsInAtlasByMember(any()))
                .willReturn(responses);

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/atlas")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("모아보기에 지도를 추가한다")
    void addTopicToAtlas_Success() throws Exception {
        // then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/atlas/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("모아보기에 추가되어있는 지도를 삭제한다")
    void removeTopicFromAtlas_Success() throws Exception {
        // then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/atlas/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
