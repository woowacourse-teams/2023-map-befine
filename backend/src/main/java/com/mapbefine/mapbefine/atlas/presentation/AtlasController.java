package com.mapbefine.mapbefine.atlas.presentation;

import com.mapbefine.mapbefine.atlas.application.AtlasCommandService;
import com.mapbefine.mapbefine.atlas.application.AtlasQueryService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atlantes")
public class AtlasController {

    private AtlasCommandService atlasCommandService;
    private AtlasQueryService atlasQueryService;

    public AtlasController(AtlasCommandService atlasCommandService, AtlasQueryService atlasQueryService) {
        this.atlasCommandService = atlasCommandService;
        this.atlasQueryService = atlasQueryService;
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<TopicResponse>> showTopics(AuthMember member) {
        List<TopicResponse> topicResponses = atlasQueryService.findAtlasByMember(member);

        return ResponseEntity.ok(topicResponses);
    }

    @LoginRequired
    @PostMapping("/{topicId}")
    public ResponseEntity<Void> addTopic(AuthMember authMember, @PathVariable Long topicId) {
        atlasCommandService.addTopic(authMember, topicId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> removeAtlas(AuthMember authMember, @PathVariable Long topicId) {
        atlasCommandService.removeTopic(authMember, topicId);

        return ResponseEntity.noContent().build();
    }
}
