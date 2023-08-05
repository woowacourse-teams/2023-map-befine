package com.mapbefine.mapbefine.member.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.application.MemberCommandService;
import com.mapbefine.mapbefine.member.application.MemberQueryService;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> findMemberById(Long memberId) {
        MemberDetailResponse response = memberQueryService.findById(memberId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMember() {
        List<MemberResponse> responses = memberQueryService.findAll();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> add(MemberCreateRequest request) {
        Long savedId = memberCommandService.save(request);

        return ResponseEntity.created(URI.create("/members/" + savedId)).build();
    }

    @GetMapping("/topics")
    public ResponseEntity<List<TopicResponse>> findTopicsByMember(AuthMember authMember) {
        List<TopicResponse> responses = memberQueryService.findTopicsByMember(authMember);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pins")
    public ResponseEntity<List<PinResponse>> findPinsByMember(AuthMember authMember) {
        List<PinResponse> responses = memberQueryService.findPinsByMember(authMember);

        return ResponseEntity.ok(responses);
    }
}
