package com.mapbefine.mapbefine.permission.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.permission.application.PermissionCommandService;
import com.mapbefine.mapbefine.permission.application.PermissionQueryService;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.permission.dto.response.PermissionMemberDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.TopicAccessDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionCommandService permissionCommandService;
    private final PermissionQueryService permissionQueryService;

    public PermissionController(
            PermissionCommandService permissionCommandService,
            PermissionQueryService permissionQueryService
    ) {
        this.permissionCommandService = permissionCommandService;
        this.permissionQueryService = permissionQueryService;
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<Void> addPermission(AuthMember authMember, @RequestBody PermissionRequest request) {
        permissionCommandService.addPermission(authMember, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deleteMemberTopicPermission(AuthMember authMember, @PathVariable Long permissionId) {
        permissionCommandService.deleteMemberTopicPermission(authMember, permissionId);

        return ResponseEntity.noContent().build();
    }

    @LoginRequired
    @GetMapping("/topics/{topicId}")
    public ResponseEntity<TopicAccessDetailResponse> findTopicAccessDetailByTopicId(@PathVariable Long topicId) {
        TopicAccessDetailResponse response = permissionQueryService.findTopicAccessDetailById(topicId);

        return ResponseEntity.ok(response);
    }

    // TODO 이 API를 쓰는 곳이 있나? + 결국 특정 회원을 조회하는 건데 어떤 API인지 알기 어렵다..
    //  회원 정보 조회는 /members 에서 하는 걸로 충분하지 않나? 재사용성이 떨어진다. 테스트의 DisplayName도 매칭이 안된다.
    @LoginRequired
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionMemberDetailResponse> findMemberTopicPermissionById(@PathVariable Long permissionId) {
        PermissionMemberDetailResponse response = permissionQueryService.findPermissionById(permissionId);

        return ResponseEntity.ok(response);
    }

}
