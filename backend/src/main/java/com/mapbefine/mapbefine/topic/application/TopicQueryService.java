package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_READ;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
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
    private final PinRepository pinRepository;
    private final MemberRepository memberRepository;

    public TopicQueryService(
            TopicRepository topicRepository,
            PinRepository pinRepository,
            MemberRepository memberRepository
    ) {
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.memberRepository = memberRepository;
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

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topicRepository.findAll().stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic)
                ))
                .toList();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("findCreatorByAuthMember; member not found; id=" + id));
    }

    private List<Topic> findTopicsInAtlas(Member member) {
        return member.getAtlantes()
                .stream()
                .map(Atlas::getTopic)
                .toList();
    }

    private List<Topic> findBookMarkedTopics(Member member) {
        return member.getBookmarks()
                .stream()
                .map(Bookmark::getTopic)
                .toList();
    }

    private boolean isInAtlas(List<Topic> topicsInAtlas, Topic topic) {
        return topicsInAtlas.contains(topic);
    }

    private boolean isBookMarked(List<Topic> bookMarkedTopics, Topic topic) {
        return bookMarkedTopics.contains(topic);
    }

    public TopicDetailResponse findDetailById(AuthMember authMember, Long topicId) {
        Topic topic = findTopic(topicId);
        validateReadableTopic(authMember, topic);

        if (Objects.isNull(authMember.getMemberId())) {
            return TopicDetailResponse.fromGuestQuery(topic);
        }

        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return TopicDetailResponse.of(
                topic,
                isInAtlas(topicsInAtlas, topic),
                isBookMarked(topicsInBookMark, topic),
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
            return getGuestTopicDetailResponses(topics);
        }

        return getUserTopicDetailResponses(authMember, topics);
    }

    private List<TopicDetailResponse> getGuestTopicDetailResponses(List<Topic> topics) {
        return topics.stream()
                .map(TopicDetailResponse::fromGuestQuery)
                .toList();
    }

    private List<TopicDetailResponse> getUserTopicDetailResponses(AuthMember authMember, List<Topic> topics) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topics.stream()
                .map(topic -> TopicDetailResponse.of(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic),
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
            return topicRepository.findByCreatorId(memberId)
                    .stream()
                    .filter(authMember::canRead)
                    .map(TopicResponse::fromGuestQuery)
                    .toList();
        }

        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topicRepository.findByCreatorId(memberId)
                .stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic)
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

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return pinRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(Pin::getTopic)
                .distinct()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic)
                )).
                toList();
    }

    private List<TopicResponse> getGuestNewestTopicResponse(AuthMember authMember) {
        return pinRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(Pin::getTopic)
                .distinct()
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

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topicRepository.findAll()
                .stream()
                .filter(authMember::canRead)
                .sorted(Comparator.comparing(Topic::countBookmarks).reversed())
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked((topicsInBookMark), topic))
                ).toList();
    }

}
