package com.mapbefine.mapbefine.topic.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.topic.dto.response.ClusteringResponse;
import com.mapbefine.mapbefine.topic.application.TopicCommandService;
import com.mapbefine.mapbefine.topic.application.TopicQueryService;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequestWithoutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequestWithoutImage;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicCommandService topicCommandService;
    private final TopicQueryService topicQueryService;

    public TopicController(
            TopicCommandService topicCommandService,
            TopicQueryService topicQueryService
    ) {
        this.topicCommandService = topicCommandService;
        this.topicQueryService = topicQueryService;
    }

    @LoginRequired
    @PostMapping(
            value = "/new",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> create(
            AuthMember member,
            @RequestPart TopicCreateRequestWithoutImage request,
            @RequestPart(required = false) MultipartFile image
    ) {
        TopicCreateRequest topicCreateRequest = TopicCreateRequest.of(request, image);
        Long topicId = topicCommandService.saveTopic(member, topicCreateRequest);

        return ResponseEntity.created(URI.create("/topics/" + topicId))
                .build();
    }

    @LoginRequired
    @PostMapping(
            value = "/merge",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> mergeAndCreate(
            AuthMember member,
            @RequestPart TopicMergeRequestWithoutImage request,
            @RequestPart(required = false) MultipartFile image
    ) {
        TopicMergeRequest topicMergeRequest = TopicMergeRequest.of(request, image);
        Long topicId = topicCommandService.merge(member, topicMergeRequest);

        return ResponseEntity.created(URI.create("/topics/" + topicId))
                .build();
    }

    @LoginRequired
    @PostMapping("/{topicId}/copy")
    public ResponseEntity<Void> copyPin(
            AuthMember member, @PathVariable Long topicId, @RequestParam List<Long> pinIds
    ) {
        topicCommandService.copyPin(member, topicId, pinIds);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAll(AuthMember member) {
        List<TopicResponse> topics = topicQueryService.findAllReadable(member);

        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDetailResponse> findById(AuthMember member, @PathVariable Long topicId) {
        TopicDetailResponse response = topicQueryService.findDetailById(member, topicId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ids")
    public ResponseEntity<List<TopicDetailResponse>> findByIds(
            AuthMember member,
            @RequestParam("ids") List<Long> topicIds
    ) {
        List<TopicDetailResponse> responses = topicQueryService.findDetailsByIds(member, topicIds);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/members")
    public ResponseEntity<List<TopicResponse>> findAllTopicsByMemberId(
            AuthMember authMember,
            @RequestParam("id") Long memberId
    ) {
        List<TopicResponse> responses = topicQueryService.findAllTopicsByMemberId(authMember, memberId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/newest")
    public ResponseEntity<List<TopicResponse>> findAllByOrderByUpdatedAtDesc(AuthMember member) {
        List<TopicResponse> responses = topicQueryService.findAllByOrderByUpdatedAtDesc(member);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @PutMapping("/{topicId}")
    public ResponseEntity<Void> update(
            AuthMember member,
            @PathVariable Long topicId,
            @RequestBody TopicUpdateRequest request
    ) {
        topicCommandService.updateTopicInfo(member, topicId, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/bests")
    public ResponseEntity<List<TopicResponse>> findAllBestTopics(AuthMember authMember) {
        List<TopicResponse> responses = topicQueryService.findAllBestTopics(authMember);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/clustering/ids")
    public ResponseEntity<List<ClusteringResponse>> getClusteringOfPins(
            AuthMember authMember,
            @RequestParam("ids") List<Long> topicIds,
            @RequestParam("image-diameter") double imageDiameter
    ) {
        List<ClusteringResponse> responses = topicQueryService.findClusteringPinsByIds(
                authMember,
                topicIds,
                imageDiameter
        );

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @PutMapping(
            value = "/images/{topicId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> deleteImage(
            AuthMember authMember,
            @PathVariable Long topicId,
            @RequestPart MultipartFile image
    ) {
        topicCommandService.updateTopicImage(authMember, topicId, image);

        return ResponseEntity.ok().build();
    }

    @Deprecated(since = "2023.10.06")
    @LoginRequired
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> delete(AuthMember member, @PathVariable Long topicId) {
        topicCommandService.delete(member, topicId);

        return ResponseEntity.noContent().build();
    }

}
