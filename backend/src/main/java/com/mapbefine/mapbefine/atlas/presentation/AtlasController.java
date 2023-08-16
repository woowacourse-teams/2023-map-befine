package com.mapbefine.mapbefine.atlas.presentation;

import com.mapbefine.mapbefine.atlas.application.AtlasCommandService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atlas")
public class AtlasController {

    private final AtlasCommandService atlasCommandService;

    public AtlasController(AtlasCommandService atlasCommandService) {
        this.atlasCommandService = atlasCommandService;
    }

    @LoginRequired
    @PostMapping("/topics")
    public ResponseEntity<Void> addTopicToAtlas(AuthMember authMember, @RequestParam Long id) {
        atlasCommandService.addTopic(authMember, id);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @DeleteMapping("/topics")
    public ResponseEntity<Void> removeTopicFromAtlas(AuthMember authMember, @RequestParam Long id) {
        atlasCommandService.removeTopic(authMember, id);

        return ResponseEntity.noContent().build();
    }

}
