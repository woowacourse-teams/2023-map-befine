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
    private PermissionQueryService permissionQueryService;
    @MockBean
    private PermissionCommandService permissionCommandService;

    @Test
    @DisplayName("권한 추가")
    void addPermission() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        PermissionRequest request = new PermissionRequest(1L, List.of(1L, 2L, 3L));
        String authHeader = testAuthHeaderProvider.createAuthHeader(member);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/permissions")
                        .header(AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한 삭제")
    void deletePermission() throws Exception {
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        String authHeader = Base64.encodeBase64String(
                ("Basic " + member.getMemberInfo().getEmail()).getBytes()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/permissions/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한이 있는 자들 모두 조회")
    void findAllTopicPermissions() throws Exception {
        List<PermissionResponse> permissionResponses = List.of(
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
                ("Basic " + permissionResponses.get(0).memberResponse().email()).getBytes()
        );

        given(permissionQueryService.findAllTopicPermissions(any())).willReturn(permissionResponses);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/permissions/topics/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한이 있는 자들 모두 조회")
    void findPermissionById() throws Exception {
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
                MockMvcRequestBuilders.get("/permissions/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
