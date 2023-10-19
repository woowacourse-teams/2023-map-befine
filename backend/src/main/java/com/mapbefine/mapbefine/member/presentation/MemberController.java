package com.mapbefine.mapbefine.member.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.member.application.MemberCommandService;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.dto.request.MemberUpdateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    public MemberController(MemberCommandService memberCommandService, MemberQueryService memberQueryService) {
        this.memberCommandService = memberCommandService;
        this.memberQueryService = memberQueryService;
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMember() {
        List<MemberResponse> responses = memberQueryService.findAll();

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/my/profiles")
    public ResponseEntity<MemberDetailResponse> findMyProfile(AuthMember authMember) {
        MemberDetailResponse response = memberQueryService.findMemberDetail(authMember);

        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @GetMapping("/my/topics")
    public ResponseEntity<List<TopicResponse>> findMyAllTopics(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findMyAllTopics(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/my/pins")
    public ResponseEntity<List<PinResponse>> findMyAllPins(AuthMember authMember) {
        List<PinResponse> pinResponses = memberQueryService.findMyAllPins(authMember);

        return ResponseEntity.ok(pinResponses);
    }

    @LoginRequired
    @GetMapping("/my/atlas")
    public ResponseEntity<List<TopicResponse>> findAllTopicsInAtlas(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findAllTopicsInAtlas(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/my/bookmarks")
    public ResponseEntity<List<TopicResponse>> findAllTopicsInBookmark(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findAllTopicsInBookmark(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @PatchMapping("/my/profiles")
    public ResponseEntity<Void> updateMyInfo(AuthMember authMember, @RequestBody MemberUpdateRequest request) {
        memberCommandService.updateInfoById(authMember, request);

        return ResponseEntity.ok().build();
    }

}
