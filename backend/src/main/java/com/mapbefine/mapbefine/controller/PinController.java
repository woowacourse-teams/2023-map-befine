package com.mapbefine.mapbefine.controller;

import com.mapbefine.mapbefine.dto.PinCreationRequest;
import com.mapbefine.mapbefine.dto.PinModificationRequest;
import com.mapbefine.mapbefine.service.PinCommandService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pins")
public class PinController {
    private final PinCommandService pinCommandService;

    public PinController(PinCommandService pinCommandService) {
        this.pinCommandService = pinCommandService;
    }

    @PostMapping
    public ResponseEntity<Void> add(PinCreationRequest request) {
        Long savedId = pinCommandService.save(request);

        return ResponseEntity.created(URI.create("/pins/" + savedId))
                .build();
    }

    @PutMapping("{pinId}")
    public ResponseEntity<Void> update(@PathVariable Long pinId, PinModificationRequest request) {
        pinCommandService.update(pinId, request);

        return ResponseEntity.ok()
                .header("Location", "/pins/" + pinId)
                .build();
    }

    @DeleteMapping("{pinId}")
    public ResponseEntity<Void> delete(@PathVariable Long pinId) {
        pinCommandService.removeById(pinId);

        return ResponseEntity.noContent()
                .build();
    }

}

