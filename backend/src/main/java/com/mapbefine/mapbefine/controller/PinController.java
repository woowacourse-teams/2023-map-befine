package com.mapbefine.mapbefine.controller;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.service.PinCommandService;
import com.mapbefine.mapbefine.service.PinQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pins")
public class PinController {

    private final PinCommandService pinCommandService;
    private final PinQueryService pinQueryService;

    public PinController(PinCommandService pinCommandService, PinQueryService pinQueryService) {
        this.pinCommandService = pinCommandService;
        this.pinQueryService = pinQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody PinCreateRequest request) {
        Long savedId = pinCommandService.save(request);

        return ResponseEntity.created(URI.create("/pins/" + savedId)).build();
    }

    @PutMapping("/{pinId}")
    public ResponseEntity<Void> update(@PathVariable Long pinId, @RequestBody PinUpdateRequest request) {
        pinCommandService.update(pinId, request);

        return ResponseEntity.ok()
                .header("Location", "/pins/" + pinId)
                .build();
    }

    @DeleteMapping("/{pinId}")
    public ResponseEntity<Void> delete(@PathVariable Long pinId) {
        pinCommandService.removeById(pinId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pinId}")
    public ResponseEntity<PinDetailResponse> findById(@PathVariable Long pinId) {
        return ResponseEntity.ok(pinQueryService.findById(pinId));
    }

    @GetMapping
    public ResponseEntity<List<PinResponse>> findAll() {
        return ResponseEntity.ok(pinQueryService.findAll());
    }

}

