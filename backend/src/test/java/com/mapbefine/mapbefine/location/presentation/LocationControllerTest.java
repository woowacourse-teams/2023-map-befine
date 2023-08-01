package com.mapbefine.mapbefine.location.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.location.application.LocationQueryService;
import com.mapbefine.mapbefine.location.dto.CoordinateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class LocationControllerTest extends RestDocsIntegration {

    @MockBean
    private LocationQueryService locationQueryService;


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
        CoordinateRequest coordinateRequest = new CoordinateRequest(37, 127);
        given(locationQueryService.findBests(any(), any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/location/best")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coordinateRequest))
        ).andDo(restDocs.document());
    }
}
