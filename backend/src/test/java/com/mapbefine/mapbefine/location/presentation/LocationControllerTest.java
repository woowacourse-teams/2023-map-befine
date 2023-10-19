package com.mapbefine.mapbefine.location.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.location.application.LocationQueryService;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class LocationControllerTest extends RestDocsIntegration {

    @MockBean
    private LocationQueryService locationQueryService;

    private List<TopicResponse> responses;

    @BeforeEach
    void setUp() {
        responses = List.of(
                new TopicResponse(
                        1L,
                        "준팍의 또 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        5,
                        Boolean.FALSE,
                        0,
                        Boolean.FALSE,
                        LocalDateTime.now()
                ), new TopicResponse(
                        2L,
                        "준팍의 두번째 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        3,
                        Boolean.FALSE,
                        0,
                        Boolean.FALSE,
                        LocalDateTime.now()
                )
        );
    }

    @Test
    @DisplayName("현재 위치를 기준 토픽의 핀 개수로 나열한다.")
    void findNearbyTopicsSortedByPinCount() throws Exception {
        //given
        double latitude = 37;
        double longitude = 127;

        //when
        given(locationQueryService.findNearbyTopicsSortedByPinCount(any(), anyDouble(), anyDouble()))
                .willReturn(responses);

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/locations/bests")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
        ).andDo(restDocs.document());
    }

}
