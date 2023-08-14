package com.mapbefine.mapbefine.member.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MemberControllerTest extends RestDocsIntegration {

    @MockBean
    private MemberQueryService memberQueryService;

    @Test
    @DisplayName("유저 목록 조회")
    void findAllMember() throws Exception {
        List<MemberResponse> memberResponses = List.of(
                new MemberResponse(
                        1L,
                        "member",
                        "member@naver.com"
                ),
                new MemberResponse(
                        2L,
                        "memberr",
                        "memberr@naver.com"
                )
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + memberResponses.get(0).email()).getBytes()
        );

        given(memberQueryService.findAll()).willReturn(memberResponses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("유저 단일 조회")
    void findMemberById() throws Exception {
        MemberDetailResponse memberDetailResponse = new MemberDetailResponse(
                1L,
                "member",
                "member@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                LocalDateTime.now()
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + memberDetailResponse.email()).getBytes()
        );

        given(memberQueryService.findById(any())).willReturn(memberDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("유저가 만든 핀 조회")
    void findPinsByMember() throws Exception {
        List<PinResponse> pinResponses = List.of(
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
        );
        String authHeader = Base64.encodeBase64String(
                "Basic member@naver.com".getBytes()
        );

        given(memberQueryService.findPinsByMember(any())).willReturn(pinResponses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/pins")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("유저가 만든 토픽 조회")
    void findTopicsByMember() throws Exception {
        String authHeader = Base64.encodeBase64String(
                "Basic member@naver.com".getBytes()
        );
        List<TopicResponse> topicResponses = List.of(
                new TopicResponse(
                        1L,
                        "준팍의 또 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        3,
                        0,
                        Boolean.FALSE,
                        LocalDateTime.now()
                ), new TopicResponse(
                        2L,
                        "준팍의 두번째 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        5,
                        0,
                        Boolean.FALSE,
                        LocalDateTime.now()
                )
        );

        given(memberQueryService.findTopicsByMember(any())).willReturn(topicResponses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/topics")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
