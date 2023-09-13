package com.mapbefine.mapbefine.admin.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.mapbefine.mapbefine.admin.application.AdminCommandService;
import com.mapbefine.mapbefine.admin.application.AdminQueryService;
import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class AdminControllerTest extends RestDocsIntegration {

    private static final List<TopicResponse> TOPIC_RESPONSES = List.of(new TopicResponse(
            1L,
            "준팍의 또 토픽",
            "https://map-befine-official.github.io/favicon.png",
            "준팍",
            3,
            Boolean.FALSE,
            5,
            Boolean.FALSE,
            LocalDateTime.now()
    ), new TopicResponse(
            2L,
            "준팍의 두번째 토픽",
            "https://map-befine-official.github.io/favicon.png",
            "준팍",
            5,
            Boolean.FALSE,
            3,
            Boolean.FALSE,
            LocalDateTime.now()
    ));
    private static final List<PinResponse> PIN_RESPONSES = List.of(
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

    private static final Member ADMIN = MemberFixture.create("Admin", "admin@naver.com", Role.ADMIN);

    @MockBean
    private AdminCommandService adminCommandService;

    @MockBean
    private AdminQueryService adminQueryService;

    @MockBean
    private MemberRepository memberRepository;

    @BeforeEach
    void setAll(){
        given(memberRepository.findById(any())).willReturn(Optional.of(ADMIN));
    }

    @DisplayName("멤버 목록 조회")
    @Test
    void findAllMemberDetails() throws Exception {
        List<AdminMemberResponse> response = List.of(
                new AdminMemberResponse(1L, "쥬니", "zuny@naver.com", "https://zuny.png", LocalDateTime.now()),
                new AdminMemberResponse(2L, "세인", "semin@naver.com", "https://semin.png", LocalDateTime.now())
        );

        given(adminQueryService.findAllMemberDetails(any())).willReturn(response);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/members")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @DisplayName("멤버 상세 조회")
    @Test
    void findMember() throws Exception {
        AdminMemberDetailResponse response = new AdminMemberDetailResponse(
                1L,
                "쥬니",
                "zuny@naver.com",
                "https://image.png",
                TOPIC_RESPONSES,
                PIN_RESPONSES,
                LocalDateTime.now()
        );

        given(adminQueryService.findMemberDetail(any(), any())).willReturn(response);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/members/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @DisplayName("멤버 차단(블랙리스트)")
    @Test
    void deleteMember() throws Exception {
        doNothing().when(adminCommandService).blockMember(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/admin/members/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @DisplayName("토픽 삭제")
    @Test
    void deleteTopic() throws Exception {
        doNothing().when(adminCommandService).deleteTopic(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/admin/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @DisplayName("토픽 이미지 삭제")
    @Test
    void deleteTopicImage() throws Exception {
        doNothing().when(adminCommandService).deleteTopicImage(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/admin/topics/1/images")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @DisplayName("핀 삭제")
    @Test
    void deletePin() throws Exception {
        doNothing().when(adminCommandService).deletePin(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/admin/pins/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @DisplayName("토픽 이미지 삭제")
    @Test
    void deletePinImage() throws Exception {
        doNothing().when(adminCommandService).deletePinImage(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/admin/pins/images/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }
}