package com.mapbefine.mapbefine.location.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
public class LocationQueryService {

    private static final double NEAR_DISTANCE_METERS = 3000;

    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    public LocationQueryService(
            LocationRepository locationRepository,
            MemberRepository memberRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.locationRepository = locationRepository;
        this.memberRepository = memberRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<TopicResponse> findNearbyTopicsSortedByPinCount(
            AuthMember authMember,
            double latitude,
            double longitude
    ) {
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        List<Location> nearLocation = locationRepository.findAllByCoordinateAndDistanceInMeters(
                coordinate.getCoordinate(),
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
        List<Long> bookmarkedTopicIds = bookmarkRepository.findAllIdTopicIdByIdMemberId(member.getId());
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return topicCounts.entrySet().stream()
                .filter(entry -> authMember.canRead(entry.getKey()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> {
                    Topic topic = entry.getKey();

                    return TopicResponse.from(
                            topic,
                            isInAtlas(topicsInAtlas, topic),
                            isInBookmark(bookmarkedTopicIds, topic.getId())
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

    private boolean isInBookmark(List<Long> bookmarkedTopics, Long topicId) {
        return bookmarkedTopics.contains(topicId);
    }

}
