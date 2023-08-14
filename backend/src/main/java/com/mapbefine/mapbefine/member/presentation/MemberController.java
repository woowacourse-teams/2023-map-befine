package com.mapbefine.mapbefine.member.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberQueryService memberQueryService;

    public MemberController(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMember() {
        List<MemberResponse> responses = memberQueryService.findAll();

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> findMemberById(@PathVariable Long memberId) {
        MemberDetailResponse response = memberQueryService.findById(memberId);

        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @GetMapping("/atlas")
    public ResponseEntity<List<TopicResponse>> findTopicsByMember(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findAllTopicsInAtlas(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @GetMapping("/bookmarks")
    public ResponseEntity<List<TopicResponse>> findPinsByMember(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findAllTopicsInBookmark(authMember);

        return ResponseEntity.ok(responses);
    }

}
