package com.mapbefine.mapbefine.controller;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.service.PinCommandService;
import com.mapbefine.mapbefine.service.PinQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class PinControllerTest extends RestDocsIntegration {
    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

    @MockBean
    private PinCommandService pinCommandService;

    @MockBean
    private PinQueryService pinQueryService;

    @Test
    @DisplayName("핀 추가")
    void add() throws Exception {
        given(pinCommandService.save(any())).willReturn(1L);

        PinCreateRequest pinCreateRequest = new PinCreateRequest(
                1L,
                "매튜의 산스장",
                "매튜가 사랑하는 산스장",
                "지번 주소",
                "법정동 코드",
                "37",
                "127",
                BASE_IMAGES
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/pins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinCreateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 수정")
    void update() throws Exception {

        PinUpdateRequest pinUpdateRequest = new PinUpdateRequest(
                "매튜의 안갈집",
                "매튜가 다신 안 갈 집",
                BASE_IMAGES
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/pins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinUpdateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 삭제")
    void delete() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/pins/1")
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 상세 조회")
    void findById() throws Exception {

        PinDetailResponse pinDetailResponse = new PinDetailResponse(
                1L,
                "매튜의 산스장",
                "지번 주소",
                "매튜가 사랑하는 산스장",
                new BigDecimal(37).toString(),
                new BigDecimal(127).toString(),
                LocalDateTime.now(),
                BASE_IMAGES
        );

        given(pinQueryService.findById(any())).willReturn(pinDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pins/1")
        ).andDo(restDocs.document());
    }


    @Test
    @DisplayName("핀 목록 조회")
    void findAll() throws Exception {

        List<PinResponse> pinResponses = List.of(
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
        );

        given(pinQueryService.findAll()).willReturn(pinResponses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pins")
        ).andDo(restDocs.document());
    }


}
