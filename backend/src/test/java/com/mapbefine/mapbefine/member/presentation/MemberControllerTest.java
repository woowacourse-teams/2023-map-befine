package com.mapbefine.mapbefine.member.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.common.interceptor.AuthInterceptor;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.application.MemberCommandService;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MemberControllerTest extends RestDocsIntegration {

    @MockBean
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @MockBean
    private AuthInterceptor authInterceptor;

    @Test
    @DisplayName("권한 추가")
    void addMemberTopicPermission() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                1L,
                2L
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + member.getMemberInfo().getEmail()).getBytes()
        );

        given(memberCommandService.save(any())).willReturn(1L);
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/members/permissions")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한 삭제")
    void deleteMemberTopicPermission() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                ("Basic " + member.getMemberInfo().getEmail()).getBytes()
        );

        given(memberCommandService.save(any())).willReturn(1L);
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/members/permissions/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한이 있는 자들 모두 조회")
    void findMemberTopicPermissionAll() throws Exception { // LoginRequired 로 인해서 RestDocs 가 막힘... Hadnler 없어서.. 그래서 200 OK 만 반환하고, Response 도 적절하게 안내뱉음
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

        given(memberQueryService.findAllWithPermission(any())).willReturn(memberResponses);
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/permissions/topics/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
