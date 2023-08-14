package com.mapbefine.mapbefine.permission.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.application.PermissionCommandService;
import com.mapbefine.mapbefine.permission.application.PermissionQueryService;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.permission.dto.response.PermissionDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class PermissionControllerTest extends RestDocsIntegration {

    @MockBean
    private PermissionCommandService permissionCommandService;
    @MockBean
    private PermissionQueryService permissionQueryService;

    @Test
    @DisplayName("권한 추가")
    void addMemberTopicPermission() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        PermissionRequest request = new PermissionRequest(1L, 2L);
        String authHeader = Base64.encodeBase64String(
                ("Basic " + member.getMemberInfo().getEmail()).getBytes()
        );

        given(permissionCommandService.savePermission(any(), any())).willReturn(1L);

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

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/members/permissions/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한이 있는 자들 모두 조회")
    void findMemberTopicPermissionAll() throws Exception {
        List<PermissionResponse> permissionRespons = List.of(
                new PermissionResponse(
                        1L,
                        new MemberResponse(
                                1L,
                                "member",
                                "member@naver.com"
                        )
                ),
                new PermissionResponse(
                        1L,
                        new MemberResponse(
                                2L,
                                "memberr",
                                "memberr@naver.com"
                        )
                )
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + permissionRespons.get(0).memberResponse().email()).getBytes()
        );

        given(permissionQueryService.findAllTopicPermissions(any())).willReturn(permissionRespons);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/permissions/topics/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한이 있는 자들 모두 조회")
    void findMemberTopicPermissionById() throws Exception {
        PermissionDetailResponse permissionDetailResponse = new PermissionDetailResponse(
                1L,
                LocalDateTime.now(),
                new MemberDetailResponse(
                        1L,
                        "member",
                        "member@naver.com",
                        "https://map-befine-official.github.io/favicon.png",
                        LocalDateTime.now()
                )
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + permissionDetailResponse.memberDetailResponse().email()).getBytes()
        );

        given(permissionQueryService.findPermissionById(any())).willReturn(permissionDetailResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/members/permissions/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
