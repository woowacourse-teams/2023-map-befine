package com.mapbefine.mapbefine.permission.presentation;

import com.mapbefine.mapbefine.common.RestDocsIntegration;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.application.PermissionCommandService;
import com.mapbefine.mapbefine.permission.application.PermissionQueryService;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.permission.dto.response.TopicAccessDetailResponse;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class PermissionControllerTest extends RestDocsIntegration {

    @MockBean
    private PermissionQueryService permissionQueryService;
    @MockBean
    private PermissionCommandService permissionCommandService;


    @Test
    @DisplayName("권한 추가")
    void addPermission() throws Exception {
        PermissionRequest request = new PermissionRequest(1L, List.of(1L, 2L, 3L));

        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("권한 삭제")
    void deletePermission() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/permissions?memberId=1&topicId=1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("특정 토픽 접근 정보 조회(권한 회원 목록, 공개 여부)")
    void findTopicAccessDetailByTopicId() throws Exception {
        List<MemberResponse> permissionedMembers = List.of(
                new MemberResponse(1L, "member"),
                new MemberResponse(2L, "memberr")
        );
        TopicAccessDetailResponse response = new TopicAccessDetailResponse(Publicity.PUBLIC, permissionedMembers);

        given(permissionQueryService.findTopicAccessDetailById(any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(restDocs.document());
    }

    // TODO: 2023/11/30 Test For Deprecated Method
//    @Test
//    @DisplayName("권한이 있는 자들 모두 조회")
//    void findPermissionById() throws Exception {
//        PermissionMemberDetailResponse permissionMemberDetailResponse = new PermissionMemberDetailResponse(
//                1L,
//                LocalDateTime.now(),
//                new MemberDetailResponse(
//                        1L,
//                        "member",
//                        "member@naver.com",
//                        "https://map-befine-official.github.io/favicon.png",
//                        LocalDateTime.now()
//                )
//        );
//
//        given(permissionQueryService.findPermissionById(any())).willReturn(permissionMemberDetailResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/permissions/1")
//                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(restDocs.document());
//    }

}
