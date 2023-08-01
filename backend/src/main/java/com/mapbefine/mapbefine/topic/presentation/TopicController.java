package com.mapbefine.mapbefine.topic.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.common.resolver.Auth;
import com.mapbefine.mapbefine.topic.application.TopicCommandService;
import com.mapbefine.mapbefine.topic.application.TopicQueryService;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
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

    @LoginRequired
    @PostMapping("/new")
    public ResponseEntity<Void> create(@Auth AuthMember member, @RequestBody TopicCreateRequest request) {
        long topicId = topicCommandService.createNew(member, request);

        return ResponseEntity.created(URI.create("/topics/" + topicId))
                .build();
    }

    @LoginRequired
    @PostMapping("/merge")
    public ResponseEntity<Void> mergeAndCreate(@Auth AuthMember member, @RequestBody TopicMergeRequest request) {
        long topicId = topicCommandService.createMerge(member, request);

        return ResponseEntity.created(URI.create("/topics/" + topicId))
                .build();
    }

    @LoginRequired
    @PutMapping("/{topicId}")
    public ResponseEntity<Void> update(
            @Auth AuthMember member,
            @PathVariable Long topicId,
            @RequestBody TopicUpdateRequest request
    ) {
        topicCommandService.updateTopicInfo(member, topicId, request);

        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> delete(@Auth AuthMember member, @PathVariable Long topicId) {
        topicCommandService.delete(member, topicId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAll(@Auth AuthMember member) {
        List<TopicResponse> topics = topicQueryService.findAll(member);

        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailResponse> findById(@Auth AuthMember member, @PathVariable Long id) {
        TopicDetailResponse response = topicQueryService.findById(member, id);

        return ResponseEntity.ok(response);
    }

}
