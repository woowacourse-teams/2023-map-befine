package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_READ;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Cluster;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.ClusteringResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicForbiddenException;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final AtlasRepository atlasRepository;
    private final BookmarkRepository bookmarkRepository;

    public TopicQueryService(
            TopicRepository topicRepository,
            MemberRepository memberRepository,
            AtlasRepository atlasRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
        this.atlasRepository = atlasRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<TopicResponse> findAllReadable(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            return getGuestTopicResponses(authMember);
        }
        return getUserTopicResponses(authMember);
    }

    private List<TopicResponse> getGuestTopicResponses(AuthMember authMember) {
        return topicRepository.findAll()
                .stream()
                .filter(authMember::canRead)
                .map(TopicResponse::fromGuestQuery)
                .toList();
    }

    private List<TopicResponse> getUserTopicResponses(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        return topicRepository.findAll().stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(member.getId(), topic.getId()),
                        isBookMarked(member.getId(), topic.getId())
                ))
                .toList();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("findCreatorByAuthMember; member not found; id=" + id));
    }

    private boolean isInAtlas(Long memberId, Long topicId) {
        return atlasRepository.existsByMemberIdAndTopicId(memberId, topicId);
    }

    private boolean isBookMarked(Long memberId, Long topicId) {
        return bookmarkRepository.existsByMemberIdAndTopicId(memberId, topicId);
    }

    public TopicDetailResponse findDetailById(AuthMember authMember, Long topicId) {
        Topic topic = findTopic(topicId);
        validateReadableTopic(authMember, topic);

        if (Objects.isNull(authMember.getMemberId())) {
            return TopicDetailResponse.fromGuestQuery(topic);
        }

        Member member = findMemberById(authMember.getMemberId());

        return TopicDetailResponse.of(
                topic,
                isInAtlas(member.getId(), topic.getId()),
                isBookMarked(member.getId(), topic.getId()),
                authMember.canTopicUpdate(topic)
        );
    }

    private Topic findTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(TOPIC_NOT_FOUND, List.of(id)));
    }

    private void validateReadableTopic(AuthMember member, Topic topic) {
        if (member.canRead(topic)) {
            return;
        }

        throw new TopicForbiddenException(FORBIDDEN_TOPIC_READ);
    }

    public List<TopicDetailResponse> findDetailsByIds(AuthMember authMember, List<Long> ids) {
        List<Topic> topics = topicRepository.findByIdIn(ids);

        validateTopicsCount(ids, topics);
        validateReadableTopics(authMember, topics);

        if (Objects.isNull(authMember.getMemberId())) {
            return TopicDetailResponse.ofGuestQueries(topics);
        }

        return getUserTopicDetailResponses(authMember, topics);
    }

    private List<TopicDetailResponse> getUserTopicDetailResponses(AuthMember authMember, List<Topic> topics) {
        return topics.stream()
                .map(topic -> TopicDetailResponse.of(
                        topic,
                        isInAtlas(authMember.getMemberId(), topic.getId()),
                        isBookMarked(authMember.getMemberId(), topic.getId()),
                        authMember.canTopicUpdate(topic)
                ))
                .toList();
    }

    private void validateTopicsCount(List<Long> topicIds, List<Topic> topics) {
        List<Long> nonMatchingIds = topicIds.stream()
                .filter(id -> topics.stream().noneMatch(topic -> topic.getId().equals(id)))
                .toList();

        if (nonMatchingIds.isEmpty()) {
            return;
        }
        throw new TopicNotFoundException(TOPIC_NOT_FOUND, nonMatchingIds);
    }

    private void validateReadableTopics(AuthMember member, List<Topic> topics) {
        int readableCount = (int) topics.stream()
                .filter(member::canRead)
                .count();

        if (topics.size() != readableCount) {
            throw new TopicForbiddenException(FORBIDDEN_TOPIC_READ);
        }
    }

    public List<TopicResponse> findAllTopicsByMemberId(AuthMember authMember, Long memberId) {

        if (Objects.isNull(authMember.getMemberId())) {
            return topicRepository.findAllByCreatorId(memberId)
                    .stream()
                    .filter(authMember::canRead)
                    .map(TopicResponse::fromGuestQuery)
                    .toList();
        }

        Member member = findMemberById(authMember.getMemberId());

        return topicRepository.findAllByCreatorId(memberId)
                .stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(member.getId(), topic.getId()),
                        isBookMarked(member.getId(), topic.getId())
                )).
                toList();
    }

    public List<TopicResponse> findAllByOrderByUpdatedAtDesc(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            return getGuestNewestTopicResponse(authMember);
        }
        return getUserNewestTopicResponse(authMember);
    }

    private List<TopicResponse> getUserNewestTopicResponse(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        return topicRepository.findAllByOrderByLastPinUpdatedAtDesc()
                .stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(member.getId(), topic.getId()),
                        isBookMarked(member.getId(), topic.getId())
                )).
                toList();
    }

    private List<TopicResponse> getGuestNewestTopicResponse(AuthMember authMember) {
        return topicRepository.findAllByOrderByLastPinUpdatedAtDesc()
                .stream()
                .filter(authMember::canRead)
                .map(TopicResponse::fromGuestQuery)
                .toList();
    }

    public List<TopicResponse> findAllBestTopics(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            return getGuestBestTopicResponse(authMember);
        }
        return getUserBestTopicResponse(authMember);
    }

    private List<TopicResponse> getGuestBestTopicResponse(AuthMember authMember) {
        return topicRepository.findAll()
                .stream()
                .filter(authMember::canRead)
                .sorted(Comparator.comparing(Topic::countBookmarks).reversed())
                .map(TopicResponse::fromGuestQuery)
                .toList();
    }

    private List<TopicResponse> getUserBestTopicResponse(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        return topicRepository.findAll()
                .stream()
                .filter(authMember::canRead)
                .sorted(Comparator.comparing(Topic::countBookmarks).reversed())
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(member.getId(), topic.getId()),
                        isBookMarked(member.getId(), topic.getId())
                )).toList();
    }

    public List<ClusteringResponse> findClusteringPinsByIds(
            AuthMember authMember,
            List<Long> topicIds,
            double realDistanceOfImage
    ) {
        // topic 을 불러온다.
        List<Topic> topics = topicRepository.findByIdIn(topicIds);

        // 토픽에 대해서 모두 검증을 진행한다.
        for (Topic topic : topics) {
            validateReadableTopic(authMember, topic);
        }

        // 검증이 모두 완료가 된 상태라면 분리집합을 진행한다.
        List<Pin> allPins = topics.stream()
                .map(Topic::getPins)
                .flatMap(List::stream)
                .toList();

        int[] parent = new int[allPins.size()];

        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < allPins.size(); i++) {
            for (int j = 0; j < allPins.size(); j++) {
                if (i == j) {
                    continue;
                }

                Pin a = allPins.get(i);
                Pin b = allPins.get(j);

                Coordinate ac = a.getLocation().getCoordinate();
                Coordinate bc = b.getLocation().getCoordinate();

                if (ac.calculateDistance(bc) <= realDistanceOfImage) {
                    union(parent, find(parent, i), find(parent, j));
                }
            }
        }

        // 분리집합이 끝나면 각각의 ID --> List<Pin> 으로 묶는다.
        Map<Integer, List<Pin>> m = new HashMap<>();
        for (int i = 0; i < allPins.size(); i++) {
            int unionNumber = find(parent, i);

            if (!m.containsKey(unionNumber)) {
                m.put(unionNumber, new ArrayList<>());
            }

            m.get(unionNumber).add(allPins.get(i));
        }

        return m.values().stream()
                .map(Cluster::from)
                .map(ClusteringResponse::from)
                .toList();
    }

    private int find(int[] parent, int x) {
        if (parent[x] == x) {
            return x;
        }

        parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private void union(int[] parent, int a, int b) {
        parent[b] = a;
    }

}
