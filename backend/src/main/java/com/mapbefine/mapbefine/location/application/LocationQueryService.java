package com.mapbefine.mapbefine.location.application;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.AuthTopic;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.location.dto.CoordinateRequest;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LocationQueryService {

    private static final double NEAR_DISTANCE = 3000.0;

    private final LocationRepository locationRepository;

    public LocationQueryService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    public List<TopicResponse> findNearbyTopicsSortedByPinCount(AuthMember member, CoordinateRequest request) {
        Coordinate coordinate = Coordinate.of(request.latitude(), request.longitude());
        List<Location> locations = locationRepository.findAllByCoordinateAndDistanceInMeters(coordinate, NEAR_DISTANCE);

        Map<Topic, Long> topicCounts = countTopicsInLocations(locations);

        return sortTopicsByCounts(topicCounts, member);
    }

    private Map<Topic, Long> countTopicsInLocations(List<Location> locations) {
        return locations.stream()
                .map(Location::getPins)
                .flatMap(Collection::stream)
                .collect(groupingBy(Pin::getTopic, counting()));
    }

    private List<TopicResponse> sortTopicsByCounts(Map<Topic, Long> topicCounts, AuthMember member) {
        return topicCounts.entrySet().stream()
                .filter(topicEntry -> canReadTopic(member, topicEntry.getKey()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .map(TopicResponse::from)
                .toList();
    }

    private boolean canReadTopic(AuthMember member, Topic topic) {
        return member.canRead(AuthTopic.from(topic));
    }

}
