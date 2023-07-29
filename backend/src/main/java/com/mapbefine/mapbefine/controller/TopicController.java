package com.mapbefine.mapbefine.controller;

import com.mapbefine.mapbefine.config.auth.AuthMember;
import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicDetailResponse;
import com.mapbefine.mapbefine.dto.TopicFindBestRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicResponse;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.service.TopicCommandService;
import com.mapbefine.mapbefine.service.TopicQueryService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicCommandService topicCommandService;
    private final TopicQueryService topicQueryService;

    public TopicController(TopicCommandService topicCommandService, TopicQueryService topicQueryService) {
        this.topicCommandService = topicCommandService;
        this.topicQueryService = topicQueryService;
    }

    @PostMapping("/new")
    public ResponseEntity<Void> create(AuthMember member, @RequestBody TopicCreateRequest request) {
        long topicId = topicCommandService.createNew(member, request);

        return ResponseEntity.created(URI.create("/topics/" + topicId))
                .build();
    }

    @PostMapping("/merge")
    public ResponseEntity<Void> mergeAndCreate(AuthMember member, @RequestBody TopicMergeRequest request) {
        long topicId = topicCommandService.createMerge(member, request);

        return ResponseEntity.created(URI.create("/topics/" + topicId))
                .build();
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<Void> update(
            AuthMember member,
            @PathVariable Long topicId,
            @RequestBody TopicUpdateRequest request
    ) {
        topicCommandService.update(member, topicId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> delete(AuthMember member, @PathVariable Long topicId) {
        topicCommandService.delete(member, topicId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAll(AuthMember member)  {
        List<TopicResponse> topics = topicQueryService.findAll(member);

        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailResponse> findById(AuthMember member, @PathVariable Long id) {
        TopicDetailResponse response = topicQueryService.findById(member, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/best")
    public ResponseEntity<List<TopicResponse>> findBests(
            AuthMember member,
            @RequestBody TopicFindBestRequest request
    ) {
        List<TopicResponse> responses = topicQueryService.findBests(member, request);

        return ResponseEntity.ok(responses);
    }
}
