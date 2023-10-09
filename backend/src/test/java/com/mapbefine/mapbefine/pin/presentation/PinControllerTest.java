package com.mapbefine.mapbefine.pin.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.pin.application.PinCommandService;
import com.mapbefine.mapbefine.pin.application.PinQueryService;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


class PinControllerTest extends RestDocsIntegration {

    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

    @MockBean
    private PinCommandService pinCommandService;

    @MockBean
    private PinQueryService pinQueryService;

    @Test
    @DisplayName("핀 생성")
    void add() throws Exception {
        given(pinCommandService.save(any(), any(), any())).willReturn(1L);

        PinCreateRequest request = new PinCreateRequest(
                1L,
                "매튜의 산스장",
                "매튜가 사랑하는 산스장",
                "지번 주소",
                "법정동 코드",
                37,
                127
        );
        MockMultipartFile requestJson = new MockMultipartFile("request", "request", APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile("image", "test.png", IMAGE_PNG_VALUE, "data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(POST, "/pins")
                        .file(image)
                        .file(requestJson)
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 수정")
    void update() throws Exception {
        PinUpdateRequest pinUpdateRequest = new PinUpdateRequest(
                "매튜의 안갈집",
                "매튜가 다신 안 갈 집"
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 삭제")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 상세 조회")
    void findById() throws Exception {
        PinDetailResponse pinDetailResponse = new PinDetailResponse(
                1L,
                "매튜의 산스장",
                "지번 주소",
                "매튜가 사랑하는 산스장",
                "매튜",
                37,
                127,
                Boolean.FALSE,
                LocalDateTime.now(),
                List.of(new PinImageResponse(1L, BASE_IMAGES.get(0)))
        );
        given(pinQueryService.findDetailById(any(), any())).willReturn(pinDetailResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
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
        );

        given(pinQueryService.findAllReadable(any())).willReturn(pinResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/pins")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 이미지 추가")
    void addImage() throws Exception {
        MockMultipartFile requestJson = new MockMultipartFile("pinId", "pinId", APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(1));
        MockMultipartFile image = new MockMultipartFile("image", "test.png", IMAGE_PNG_VALUE, "data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(POST, "/pins/images")
                        .file(image)
                        .file(requestJson)
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 이미지 삭제")
    void removeImage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/pins/images/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원 Id를 입력하면 해당 회원이 만든 핀 목록을 조회할 수 있다.")
    void findAllPinsByMemberId() throws Exception {
        List<PinResponse> pinResponses = List.of(
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
        );

        given(pinQueryService.findAllPinsByMemberId(any(), any())).willReturn(pinResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/pins/members?id=1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

}
