package com.mapbefine.mapbefine.pin.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.application.PinCommandService;
import com.mapbefine.mapbefine.pin.application.PinQueryService;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


class PinControllerTest extends RestDocsIntegration {

    private static final String BASIC_FORMAT = "Basic %s";

    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

    @MockBean
    private PinCommandService pinCommandService;

    @MockBean
    private PinQueryService pinQueryService;

    @Test
    @DisplayName("핀 추가")
    void add() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );
        given(pinCommandService.save(any(), any())).willReturn(1L);

        PinCreateRequest pinCreateRequest = new PinCreateRequest(
                1L,
                "매튜의 산스장",
                "매튜가 사랑하는 산스장",
                "지번 주소",
                "법정동 코드",
                37,
                127
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/pins")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinCreateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 수정")
    void update() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

        PinUpdateRequest pinUpdateRequest = new PinUpdateRequest(
                "매튜의 안갈집",
                "매튜가 다신 안 갈 집"
        );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/pins/1")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinUpdateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 삭제")
    void delete() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/pins/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 상세 조회")
    void findById() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

        PinDetailResponse pinDetailResponse = new PinDetailResponse(
                1L,
                "매튜의 산스장",
                "지번 주소",
                "매튜가 사랑하는 산스장",
                "매튜",
                37,
                127,
                LocalDateTime.now(),
                List.of(new PinImageResponse(1L, BASE_IMAGES.get(0)))
        );

        given(pinQueryService.findDetailById(any(), any())).willReturn(pinDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pins/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName(AUTHORIZATION).optional().description("Optional")
                )));
    }


    @Test
    @DisplayName("핀 목록 조회")
    void findAll() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

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
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document(
                requestHeaders(
                        headerWithName(AUTHORIZATION).optional().description("Optional")
                )));
    }

    @Test
    @DisplayName("핀 이미지 추가")
    void addImage() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

        PinImageCreateRequest pinImageCreateRequest = new PinImageCreateRequest(
                1L,
                "https://map-befine-official.github.io/favicon.png"
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/pins/images")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinImageCreateRequest))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("핀 이미지 삭제")
    void removeImage() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/pins/images/1")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(restDocs.document());
    }


    @Test
    @DisplayName("멤버 Id를 입력하면 해당 멤버가 만든 핀 목록을 조회할 수 있다.")
    void findAllPinsByMemberId() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                String.format(BASIC_FORMAT, member.getMemberInfo().getEmail()).getBytes()
        );

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

                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }


}
