package com.mapbefine.mapbefine.controller;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.service.PinCommandService;
import com.mapbefine.mapbefine.service.PinQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        PinDetailResponse response = pinQueryService.findById(pinId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PinResponse>> findAll() {
        List<PinResponse> allResponses = pinQueryService.findAll();

        return ResponseEntity.ok(allResponses);
    }

}


