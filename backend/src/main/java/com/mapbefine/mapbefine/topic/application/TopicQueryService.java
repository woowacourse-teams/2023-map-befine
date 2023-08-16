package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND_EXCEPTION;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.domain.TopicWithBookmarkStatus;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicNotFoundException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

    private final TopicRepository topicRepository;

    public TopicQueryService(final TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<TopicResponse> findAllReadable(AuthMember member) {
        if (Objects.isNull(member.getMemberId())) {
            return findAllWithoutBookmarkStatus(member);
        }

        return findAllWithBookmarkStatus(member);
    }

    private List<TopicResponse> findAllWithoutBookmarkStatus(AuthMember member) {
        return topicRepository.findAll()
                .stream()
                .filter(member::canRead)
                .map(topic -> TopicResponse.from(topic, Boolean.FALSE))
                .toList();
    }

    private List<TopicResponse> findAllWithBookmarkStatus(AuthMember member) {
        return topicRepository.findAllWithBookmarkStatusByMemberId(member.getMemberId())
                .stream()
                .filter(topicWithBookmark -> member.canRead(topicWithBookmark.getTopic()))
                .map(topicWithBookmark -> TopicResponse.from(
                        topicWithBookmark.getTopic(),
                        topicWithBookmark.getIsBookmarked()
                ))
                .toList();
    }

    public TopicDetailResponse findDetailById(AuthMember member, Long topicId) {
        if (Objects.isNull(member.getMemberId())) {
            return findWithoutBookmarkStatus(member, topicId);
        }

        return findWithBookmarkStatus(member, topicId);
    }

    private TopicDetailResponse findWithoutBookmarkStatus(AuthMember member, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException(TOPIC_NOT_FOUND_EXCEPTION, topicId));

        validateReadableTopic(member, topic);

        return TopicDetailResponse.from(topic, Boolean.FALSE);
    }

    private void validateReadableTopic(AuthMember member, Topic topic) {
        if (member.canRead(topic)) {
            return;
        }

        throw new IllegalArgumentException("조회권한이 없는 Topic 입니다.");
    }

    private TopicDetailResponse findWithBookmarkStatus(AuthMember member, Long topicId) {
        TopicWithBookmarkStatus topicWithBookmarkStatus =
                topicRepository.findWithBookmarkStatusByIdAndMemberId(topicId, member.getMemberId())
                        .orElseThrow(() -> new TopicNotFoundException(TOPIC_NOT_FOUND_EXCEPTION, topicId));

        validateReadableTopic(member, topicWithBookmarkStatus.getTopic());

        return TopicDetailResponse.from(
                topicWithBookmarkStatus.getTopic(),
                topicWithBookmarkStatus.getIsBookmarked()
        );
    }

    public List<TopicDetailResponse> findDetailsByIds(AuthMember member, List<Long> topicIds) {
        if (Objects.isNull(member.getMemberId())) {
            return findDetailsWithoutBookmarkStatus(member, topicIds);
        }

        return findDetailsWithBookmarkStatus(member, topicIds);
    }

    private List<TopicDetailResponse> findDetailsWithoutBookmarkStatus(AuthMember member,
            List<Long> topicIds) {
        List<Topic> topics = topicRepository.findByIdIn(topicIds);

        validateTopicsCount(topicIds, topics);
        validateReadableTopics(member, topics);

        return topics.stream()
                .map(topic -> TopicDetailResponse.from(topic, Boolean.FALSE))
                .toList();
    }

    private void validateTopicsCount(List<Long> topicIds, List<Topic> topics) {
        if (topicIds.size() != topics.size()) {
            throw new IllegalArgumentException("존재하지 않는 토픽이 존재합니다");
        }
    }

    private void validateReadableTopics(AuthMember member, List<Topic> topics) {
        int readableCount = (int) topics.stream()
                .filter(member::canRead)
                .count();

        if (topics.size() != readableCount) {
            throw new IllegalArgumentException("읽을 수 없는 토픽이 존재합니다.");
        }
    }

    private List<TopicDetailResponse> findDetailsWithBookmarkStatus(
            AuthMember member,
            List<Long> topicIds
    ) {
        List<TopicWithBookmarkStatus> topicsWithBookmarkStatus =
                topicRepository.findWithBookmarkStatusByIdsAndMemberId(
                        topicIds,
                        member.getMemberId()
                );

        List<Topic> topics = topicsWithBookmarkStatus.stream()
                .map(TopicWithBookmarkStatus::getTopic)
                .toList();

        validateTopicsCount(topicIds, topics);
        validateReadableTopics(member, topics);

        return topicsWithBookmarkStatus.stream()
                .map(topicWithBookmarkStatus -> TopicDetailResponse.from(
                        topicWithBookmarkStatus.getTopic(),
                        topicWithBookmarkStatus.getIsBookmarked()
                ))
                .toList();
    }

}
