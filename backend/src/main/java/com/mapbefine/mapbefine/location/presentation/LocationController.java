package com.mapbefine.mapbefine.location.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.location.application.LocationQueryService;
import com.mapbefine.mapbefine.location.dto.CoordinateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationQueryService locationQueryService;

    public LocationController(LocationQueryService locationQueryService) {
        this.locationQueryService = locationQueryService;
    }

    @GetMapping("/bests")
    public ResponseEntity<List<TopicResponse>> findNearbyTopicsSortedByPinCount(
            AuthMember member,
            @RequestBody CoordinateRequest request
    ) {
        List<TopicResponse> responses = locationQueryService.findNearbyTopicsSortedByPinCount(member, request);

        return ResponseEntity.ok(responses);
    }

}
