package com.mapbefine.mapbefine.pin.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.pin.application.PinCommandService;
import com.mapbefine.mapbefine.pin.application.PinQueryService;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/pins")
public class PinController {

    private final PinCommandService pinCommandService;
    private final PinQueryService pinQueryService;

    public PinController(PinCommandService pinCommandService, PinQueryService pinQueryService) {
        this.pinCommandService = pinCommandService;
        this.pinQueryService = pinQueryService;
    }

    @LoginRequired
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> add(
            AuthMember member,
            @RequestPart List<MultipartFile> images,
            @RequestPart PinCreateRequest request
    ) {
        long savedId = pinCommandService.save(member, images, request);

        return ResponseEntity.created(URI.create("/pins/" + savedId))
                .build();
    }

    @LoginRequired
    @PutMapping("/{pinId}")
    public ResponseEntity<Void> update(
            AuthMember member,
            @PathVariable Long pinId,
            @RequestBody PinUpdateRequest request
    ) {
        pinCommandService.update(member, pinId, request);

        return ResponseEntity.ok()
                .header("Location", "/pins/" + pinId)
                .build();
    }

    @LoginRequired
    @DeleteMapping("/{pinId}")
    public ResponseEntity<Void> delete(AuthMember member, @PathVariable Long pinId) {
        pinCommandService.removeById(member, pinId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pinId}")
    public ResponseEntity<PinDetailResponse> findById(AuthMember member, @PathVariable Long pinId) {
        PinDetailResponse response = pinQueryService.findDetailById(member, pinId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PinResponse>> findAll(AuthMember member) {
        List<PinResponse> allResponses = pinQueryService.findAllReadable(member);

        return ResponseEntity.ok(allResponses);
    }

    @GetMapping("/members")
    public ResponseEntity<List<PinResponse>> findAllPinsByMemberId(
            AuthMember authMember,
            @RequestParam("id") Long memberId

    ) {
        List<PinResponse> responses = pinQueryService.findAllPinsByMemberId(authMember, memberId);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @PostMapping(
            value = "/images",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> addImage(
            AuthMember member,
            @RequestPart Long pinId,
            @RequestPart MultipartFile image
    ) {
        pinCommandService.addImage(member, new PinImageCreateRequest(pinId, image));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @DeleteMapping("/images/{pinImageId}")
    public ResponseEntity<Void> removeImage(AuthMember member, @PathVariable Long pinImageId) {
        pinCommandService.removeImageById(member, pinImageId);

        return ResponseEntity.noContent().build();
    }

}


