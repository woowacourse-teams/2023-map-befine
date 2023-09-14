package com.mapbefine.mapbefine.pin.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

import com.mapbefine.mapbefine.FileFixture;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.pin.application.PinCommandService;
import com.mapbefine.mapbefine.pin.application.PinQueryService;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


class PinControllerTest extends RestDocsIntegration {

    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

    @MockBean
    private PinCommandService pinCommandService;

    @MockBean
    private PinQueryService pinQueryService;

    @Test
    @DisplayName("핀 추가")
    void add() throws Exception {
        given(pinCommandService.save(any(), any(), any())).willReturn(1L);
        File mockFile = new File(getClass().getClassLoader().getResource("test.png").getPath());
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();

        PinCreateRequest pinCreateRequest = new PinCreateRequest(
                1L,
                "매튜의 산스장",
                "매튜가 사랑하는 산스장",
                "지번 주소",
                "법정동 코드",
                37,
                127
        );

        param.add("images", List.of(mockFile));
        param.add("request", pinCreateRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/pins")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(objectMapper.writeValueAsString(param))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 수정")
    void update() throws Exception {
        PinUpdateRequest pinUpdateRequest = new PinUpdateRequest(
                "매튜의 안갈집",
                "매튜가 다신 안 갈 집"
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinUpdateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 삭제")
    void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
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
                "매튜",
                37,
                127,
                Boolean.FALSE,
                LocalDateTime.now(),
                List.of(new PinImageResponse(1L, BASE_IMAGES.get(0)))
        );

        given(pinQueryService.findDetailById(any(), any())).willReturn(pinDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName(AUTHORIZATION).optional().description("Optional")
                )));
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

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pins")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName(AUTHORIZATION).optional().description("Optional")
                )));
    }

    @Test
    @DisplayName("핀 이미지 추가")
    void addImage() throws Exception {
        String imageFilePath = getClass().getClassLoader()
                .getResource("test.png")
                .getPath();
        File mockFile = new File(imageFilePath);

//        PinImageCreateRequest pinImageCreateRequest = new PinImageCreateRequest(
//                1L,
//                FileFixture.createFile()
//        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/pins/images")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L))
                        .content(objectMapper.writeValueAsString(mockFile))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 이미지 삭제")
    void removeImage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/pins/images/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(restDocs.document());
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

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pins/members?id=1")

                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

}
