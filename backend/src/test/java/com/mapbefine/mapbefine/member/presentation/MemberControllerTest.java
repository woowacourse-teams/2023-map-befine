package com.mapbefine.mapbefine.member.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.dto.request.MemberUpdateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MemberControllerTest extends RestDocsIntegration {

    @MockBean
    private MemberQueryService memberQueryService;

    @Test
    @DisplayName("회원 목록 조회")
    void findAllMember() throws Exception {
        List<MemberResponse> memberResponses = List.of(
                new MemberResponse(
                        1L,
                        "member1"
                ),
                new MemberResponse(
                        2L,
                        "member2"
                )
        );

        given(memberQueryService.findAll()).willReturn(memberResponses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원 단일 조회")
    void findMemberById() throws Exception {
        MemberDetailResponse memberDetailResponse = new MemberDetailResponse(
                1L,
                "member",
                "member@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                LocalDateTime.now()
        );

        given(memberQueryService.findById(any())).willReturn(memberDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원의 모아보기 목록 조회")
    void findAllTopicsInAtlas() throws Exception {
        List<TopicResponse> responses = List.of(
                new TopicResponse(
                        1L,
                        "준팍의 또 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        5,
                        Boolean.TRUE,
                        0,
                        Boolean.FALSE,
                        LocalDateTime.now()
                ), new TopicResponse(
                        2L,
                        "준팍의 두번째 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        3,
                        Boolean.TRUE,
                        0,
                        Boolean.FALSE,
                        LocalDateTime.now()
                )
        );

        given(memberQueryService.findAllTopicsInAtlas(any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/my/atlas")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원의 즐겨찾기 목록 조회")
    void findAllTopicsInBookmark() throws Exception {
        List<TopicResponse> responses = List.of(
                new TopicResponse(
                        1L,
                        "준팍의 또 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        5,
                        Boolean.FALSE,
                        0,
                        Boolean.TRUE,
                        LocalDateTime.now()
                ), new TopicResponse(
                        2L,
                        "준팍의 두번째 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        3,
                        Boolean.FALSE,
                        0,
                        Boolean.TRUE,
                        LocalDateTime.now()
                )
        );

        given(memberQueryService.findAllTopicsInBookmark(any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/my/bookmarks")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원의 지도 목록 조회")
    void findMyAllTopics() throws Exception {
        List<TopicResponse> responses = List.of(
                new TopicResponse(
                        1L,
                        "준팍의 또 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍",
                        5,
                        Boolean.FALSE,
                        0,
                        Boolean.TRUE,
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

        given(memberQueryService.findMyAllTopics(any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/my/topics")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원의 핀 목록 조회")
    void findMyAllPins() throws Exception {
        List<PinResponse> responses = List.of(
                new PinResponse(
                        1L,
                        "준팍의 첫번째 핀",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍의 첫번째 핀",
                        "준팍",
                        LocationFixture.BASE_COORDINATE.getLatitude(),
                        LocationFixture.BASE_COORDINATE.getLongitude()
                ), new PinResponse(
                        2L,
                        "준팍의 두번째 핀",
                        "https://map-befine-official.github.io/favicon.png",
                        "준팍의 두번째 핀",
                        "준팍",
                        LocationFixture.BASE_COORDINATE.getLatitude(),
                        LocationFixture.BASE_COORDINATE.getLongitude()
                )
        );

        given(memberQueryService.findMyAllPins(any())).willReturn(responses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/my/pins")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원의 정보 수정")
    void updateMyInfo() throws Exception {
        MemberUpdateRequest request = new MemberUpdateRequest("새로운 닉네임");

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/members/my/profiles")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(restDocs.document());
    }

}
