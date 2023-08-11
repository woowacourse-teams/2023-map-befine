package com.mapbefine.mapbefine.member.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.member.application.MemberCommandService;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public MemberController(
            MemberCommandService memberCommandService,
            MemberQueryService memberQueryService
    ) {
        this.memberCommandService = memberCommandService;
        this.memberQueryService = memberQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody MemberCreateRequest request) {
        Long savedId = memberCommandService.save(request);

        return ResponseEntity.created(URI.create("/members/" + savedId)).build();
    }

    @LoginRequired
    @PostMapping("/permissions")
    public ResponseEntity<Void> addMemberTopicPermission(
            AuthMember authMember,
            @RequestBody MemberTopicPermissionCreateRequest request
    ) {
        Long savedId = memberCommandService.saveMemberTopicPermission(authMember, request);

        return ResponseEntity.created(URI.create("/members/permissions/" + savedId)).build();
    }

    @LoginRequired
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> findMemberById(@PathVariable Long memberId) {
        MemberDetailResponse response = memberQueryService.findById(memberId);

        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMember() {
        List<MemberResponse> responses = memberQueryService.findAll();

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/topics")
    public ResponseEntity<List<TopicResponse>> findTopicsByMember(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findTopicsByMember(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/pins")
    public ResponseEntity<List<PinResponse>> findPinsByMember(AuthMember authMember) {
        List<PinResponse> responses = memberQueryService.findPinsByMember(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/permissions/topics/{topicId}")
    public ResponseEntity<List<MemberTopicPermissionResponse>> findMemberTopicPermissionAll(
            @PathVariable Long topicId
    ) {
        List<MemberTopicPermissionResponse> responses =
                memberQueryService.findAllWithPermission(topicId);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/permissions/{permissionId}")
    public ResponseEntity<MemberTopicPermissionDetailResponse> findMemberTopicPermissionById(
            @PathVariable Long permissionId
    ) {
        MemberTopicPermissionDetailResponse response =
                memberQueryService.findMemberTopicPermissionById(permissionId);

        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @DeleteMapping("/permissions/{permissionId}")
    public ResponseEntity<Void> deleteMemberTopicPermission(
            AuthMember authMember,
            @PathVariable Long permissionId
    ) {
        memberCommandService.deleteMemberTopicPermission(authMember, permissionId);

        return ResponseEntity.noContent().build();
    }

}
