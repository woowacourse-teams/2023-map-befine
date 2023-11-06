package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_READ;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Clusters;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.ClusterResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicForbiddenException;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicNotFoundException;
import java.util.Comparator;
import java.util.List;
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

    public List<ClusterResponse> findClustersPinsByIds(
            AuthMember authMember,
            List<Long> topicIds,
            Double imageDiameter
    ) {
        List<Topic> topics = topicRepository.findByIdIn(topicIds);

        for (Topic topic : topics) {
            validateReadableTopic(authMember, topic);
        }

        List<Pin> allPins = topics.stream()
                .map(Topic::getPins)
                .flatMap(List::stream)
                .toList();

        return Clusters.from(allPins, imageDiameter)
                .getClusters()
                .stream()
                .map(ClusterResponse::from)
                .toList();
    }

}
