package com.mapbefine.mapbefine.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.mapbefine.mapbefine.config.auth.AuthMember;
import com.mapbefine.mapbefine.config.auth.AuthTopic;
import com.mapbefine.mapbefine.dto.TopicDetailResponse;
import com.mapbefine.mapbefine.dto.TopicFindBestRequest;
import com.mapbefine.mapbefine.dto.TopicResponse;
import com.mapbefine.mapbefine.entity.pin.Location;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.topic.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

    private final TopicRepository topicRepository;
    private final LocationRepository locationRepository;

    public TopicQueryService(final TopicRepository topicRepository, LocationRepository locationRepository) {
        this.topicRepository = topicRepository;
        this.locationRepository = locationRepository;
    }

    public List<TopicResponse> findAll(AuthMember member) {
        return topicRepository.findAll().stream()
                .filter(topic -> member.canRead(AuthTopic.from(topic)))
                .map(TopicResponse::from)
                .toList();
    }

    public TopicDetailResponse findById(AuthMember member, Long id) {
        Topic topic = topicRepository.findById(id)
                .stream()
                .filter(presentTopic -> member.canRead(AuthTopic.from(presentTopic)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Topic이 존재하지 않습니다."));

        return TopicDetailResponse.from(topic);
    }

    public List<TopicResponse> findBests(AuthMember member, TopicFindBestRequest request) {
        List<Location> locations = findLocationsInRectangle(request);
        Map<Topic, Long> topicCounts = countTopicsInLocations(locations);

        return sortTopicsByCounts(topicCounts, member);
    }

    private List<Location> findLocationsInRectangle(TopicFindBestRequest request) {
        return locationRepository.findAllByRectangle(
                new BigDecimal(request.latitude()),
                new BigDecimal(request.longitude()),
                BigDecimal.valueOf(0.03)
        );
    }

    private Map<Topic, Long> countTopicsInLocations(List<Location> locations) {
        return locations.stream()
                .map(Location::getPins)
                .flatMap(Collection::stream)
                .collect(groupingBy(Pin::getTopic, counting()));
    }

    private List<TopicResponse> sortTopicsByCounts(Map<Topic, Long> topicCounts, AuthMember member) {
        return topicCounts.entrySet().stream()
                .filter(topicEntry -> member.canRead(AuthTopic.from(topicEntry.getKey()))) // TODO : 볼 수 있는 토픽만 걸러내는 과정
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .map(TopicResponse::from)
                .collect(Collectors.toList());
    }

}
