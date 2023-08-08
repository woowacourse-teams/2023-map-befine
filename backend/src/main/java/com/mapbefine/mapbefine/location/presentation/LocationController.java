package com.mapbefine.mapbefine.location.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.location.application.LocationQueryService;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude

    ) {
        List<TopicResponse> responses = locationQueryService.findNearbyTopicsSortedByPinCount(
                member,
                latitude,
                longitude
        );

        return ResponseEntity.ok(responses);
    }

}
