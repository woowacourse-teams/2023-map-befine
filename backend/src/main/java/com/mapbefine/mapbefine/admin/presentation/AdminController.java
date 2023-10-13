package com.mapbefine.mapbefine.admin.presentation;

import com.mapbefine.mapbefine.admin.application.AdminCommandService;
import com.mapbefine.mapbefine.admin.application.AdminQueryService;
import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<List<AdminMemberResponse>> findAllMembers() {
        List<AdminMemberResponse> responses = adminQueryService.findAllMemberDetails();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        adminCommandService.blockMember(memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<AdminMemberDetailResponse> findMember(@PathVariable Long memberId) {
        AdminMemberDetailResponse response = adminQueryService.findMemberDetail(memberId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/topics/{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long topicId) {
        adminCommandService.deleteTopic(topicId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/topics/{topicId}/images")
    public ResponseEntity<Void> deleteTopicImage(@PathVariable Long topicId) {
        adminCommandService.deleteTopicImage(topicId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pins/{pinId}")
    public ResponseEntity<Void> deletePin(@PathVariable Long pinId) {
        adminCommandService.deletePin(pinId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pins/images/{imageId}")
    public ResponseEntity<Void> deletePinImage(@PathVariable Long imageId) {
        adminCommandService.deletePinImage(imageId);

        return ResponseEntity.noContent().build();
    }

}
