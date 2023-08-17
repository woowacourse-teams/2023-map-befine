package com.mapbefine.mapbefine.location.application;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LocationQueryService {

    private static final double NEAR_DISTANCE_METERS = 3000;

    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;

    public LocationQueryService(LocationRepository locationRepository, MemberRepository memberRepository) {
        this.locationRepository = locationRepository;
        this.memberRepository = memberRepository;
    }

    public List<TopicResponse> findNearbyTopicsSortedByPinCount(
            AuthMember authMember,
            double latitude,
            double longitude
    ) {
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        List<Location> nearLocation = locationRepository.findAllByCoordinateAndDistanceInMeters(
                coordinate,
                NEAR_DISTANCE_METERS
        );

        Map<Topic, Long> pinCountsByTopic = countPinsByTopicInLocations(nearLocation);

        return sortTopicsByPinCounts(pinCountsByTopic, authMember);
    }

    private Map<Topic, Long> countPinsByTopicInLocations(List<Location> locations) {
        return locations.stream()
                .map(Location::getPins)
                .flatMap(Collection::stream)
                .collect(groupingBy(Pin::getTopic, counting()));
    }

    private List<TopicResponse> sortTopicsByPinCounts(
            Map<Topic, Long> topicCounts,
            AuthMember authMember
    ) {
        if (Objects.isNull(authMember.getMemberId())) {
            getUserTopicResponses(topicCounts, authMember);
        }

        return getGuestTopicResponse(topicCounts, authMember);
    }

    private List<TopicResponse> getUserTopicResponses(final Map<Topic, Long> topicCounts, final AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());
        List<Topic> bookmarkedTopics = findBookMarkedTopics(member);
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return topicCounts.entrySet().stream()
                .filter(entry -> authMember.canRead(entry.getKey()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> {
                    Topic topic = entry.getKey();

                    return TopicResponse.from(
                            topic,
                            isInAtlas(topicsInAtlas, topic),
                            isInBookmark(bookmarkedTopics, topic)
                    );
                })
                .toList();
    }

    private List<TopicResponse> getGuestTopicResponse(Map<Topic, Long> topicCounts, AuthMember authMember) {
        return topicCounts.entrySet().stream()
                .filter(entry -> authMember.canRead(entry.getKey()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> {
                    Topic topic = entry.getKey();

                    return TopicResponse.from(
                            topic,
                            Boolean.FALSE,
                            Boolean.FALSE
                    );
                })
                .toList();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("findMemberById; memberId= " + id));
    }

    private List<Topic> findTopicsInAtlas(Member member) {
        return member.getAtlantes()
                .stream()
                .map(Atlas::getTopic)
                .toList();
    }

    private boolean isInAtlas(List<Topic> topicsInAtlas, Topic topic) {
        return topicsInAtlas.contains(topic);
    }

    private List<Topic> findBookMarkedTopics(Member member) {
        return member.getBookmarks()
                .stream()
                .map(Bookmark::getTopic)
                .toList();
    }

    private boolean isInBookmark(List<Topic> bookmarkedTopics, Topic topic) {
        return bookmarkedTopics.contains(topic);
    }

}
