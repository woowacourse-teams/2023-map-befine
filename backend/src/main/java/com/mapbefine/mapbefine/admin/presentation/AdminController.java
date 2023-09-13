package com.mapbefine.mapbefine.admin.presentation;

import com.mapbefine.mapbefine.admin.application.AdminCommandService;
import com.mapbefine.mapbefine.admin.application.AdminQueryService;
import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminQueryService adminQueryService;
    private final AdminCommandService adminCommandService;


    public AdminController(AdminQueryService adminQueryService, AdminCommandService adminCommandService) {
        this.adminQueryService = adminQueryService;
        this.adminCommandService = adminCommandService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<AdminMemberResponse>> findAllMembers(AuthMember authMember) {
        List<AdminMemberResponse> responses = adminQueryService.findAllMemberDetails(authMember);

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMember(AuthMember authMember, @PathVariable Long memberId) {
        adminCommandService.blockMember(authMember, memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<AdminMemberDetailResponse> findMember(AuthMember authMember, @PathVariable Long memberId) {
        AdminMemberDetailResponse response = adminQueryService.findMemberDetail(authMember, memberId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/topics/{topicId}")
    public ResponseEntity<Void> deleteTopic(AuthMember authMember, @PathVariable Long topicId) {
        adminCommandService.deleteTopic(authMember, topicId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/topics/{topicId}/images")
    public ResponseEntity<Void> deleteTopicImage(AuthMember authMember, @PathVariable Long topicId) {
        adminCommandService.deleteTopicImage(authMember, topicId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pins/{pinId}")
    public ResponseEntity<Void> deletePin(AuthMember authMember, @PathVariable Long pinId) {
        adminCommandService.deletePin(authMember, pinId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pins/images/{imageId}")
    public ResponseEntity<Void> deletePinImage(AuthMember authMember, @PathVariable Long imageId) {
        adminCommandService.deletePinImage(authMember, imageId);

        return ResponseEntity.noContent().build();
    }

}
