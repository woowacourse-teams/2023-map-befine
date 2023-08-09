package com.mapbefine.mapbefine.topic.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
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
        return topicRepository.findAll().stream()
                .filter(member::canRead)
                .map(TopicResponse::from)
                .toList();
    }

    public TopicDetailResponse findDetailById(AuthMember member, Long id) {
        Topic topic = findTopic(id);

        validateReadableTopic(member, topic);

        return TopicDetailResponse.from(topic);
    }

    private Topic findTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Topic이 존재하지 않습니다."));
    }

    private void validateReadableTopic(AuthMember member, Topic topic) {
        if (member.canRead(topic)) {
            return;
        }

        throw new IllegalArgumentException("조회권한이 없는 Topic 입니다.");

    }

    public List<TopicDetailResponse> findDetailsByIds(AuthMember member, List<Long> ids) {
        List<Topic> topics = topicRepository.findByIdIn(ids);

        validateTopicsCount(ids, topics);
        validateReadableTopics(member, topics);

        return topics.stream()
                .map(TopicDetailResponse::from)
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

}
