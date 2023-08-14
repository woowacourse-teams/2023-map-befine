package com.mapbefine.mapbefine.location.application;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
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

    private static final double NEAR_DISTANCE_METERS = 3000;

    private final LocationRepository locationRepository;
    private final AtlasRepository atlasRepository;

    public LocationQueryService(LocationRepository locationRepository, AtlasRepository atlasRepository) {
        this.locationRepository = locationRepository;
        this.atlasRepository = atlasRepository;
    }

    public List<TopicResponse> findNearbyTopicsSortedByPinCount(
            AuthMember member,
            double latitude,
            double longitude
    ) {
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        List<Location> nearLocation = locationRepository.findAllByCoordinateAndDistanceInMeters(
                coordinate,
                NEAR_DISTANCE_METERS
        );

        Map<Topic, Long> pinCountsByTopic = countPinsByTopicInLocations(nearLocation);

        return sortTopicsByPinCounts(pinCountsByTopic, member);
    }

    private Map<Topic, Long> countPinsByTopicInLocations(List<Location> locations) {
        return locations.stream()
                .map(Location::getPins)
                .flatMap(Collection::stream)
                .collect(groupingBy(Pin::getTopic, counting()));
    }

    private List<TopicResponse> sortTopicsByPinCounts(Map<Topic, Long> topicCounts, AuthMember member) {
        List<Topic> topicsInAtlas = getTopicsInAtlas(member);

        return topicCounts.entrySet().stream()
                .filter(entry -> member.canRead(entry.getKey()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> {
                    Topic topic = entry.getKey();
                    return TopicResponse.from(topic, isInAtlas(topicsInAtlas, topic));
                })
                .toList();
    }


    private List<Topic> getTopicsInAtlas(AuthMember member) {
        return atlasRepository.findAllByMemberId(member.getMemberId())
                .stream()
                .map(Atlas::getTopic)
                .toList();
    }

    private boolean isInAtlas(List<Topic> topicsInAtlas, Topic topic) {
        return topicsInAtlas.contains(topic);
    }

}
