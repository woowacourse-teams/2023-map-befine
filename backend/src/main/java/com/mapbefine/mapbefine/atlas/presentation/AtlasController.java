package com.mapbefine.mapbefine.atlas.presentation;

import com.mapbefine.mapbefine.atlas.application.AtlasCommandService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atlas")
public class AtlasController {

    private final AtlasCommandService atlasCommandService;

    public AtlasController(AtlasCommandService atlasCommandService) {
        this.atlasCommandService = atlasCommandService;
    }

    @LoginRequired
    @PostMapping("/{topicId}")
    public ResponseEntity<Void> addTopicToAtlas(AuthMember authMember, @PathVariable Long topicId) {
        atlasCommandService.addTopic(authMember, topicId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> removeTopicFromAtlas(AuthMember authMember, @PathVariable Long topicId) {
        atlasCommandService.removeTopic(authMember, topicId);

        return ResponseEntity.noContent().build();
    }

}
