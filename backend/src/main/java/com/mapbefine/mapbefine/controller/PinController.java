package com.mapbefine.mapbefine.controller;

import com.mapbefine.mapbefine.dto.PinCreationRequest;
import com.mapbefine.mapbefine.service.PinCommandService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Void> add(PinCreationRequest pinCreationRequest) {
        Long savedId = pinCommandService.save(pinCreationRequest);

        return ResponseEntity.created(URI.create("/pins/" + savedId)).build();
    }
}

