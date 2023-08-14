package com.mapbefine.mapbefine.topic.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
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
    private final AtlasRepository atlasRepository;

    public TopicQueryService(TopicRepository topicRepository, AtlasRepository atlasRepository) {
        this.topicRepository = topicRepository;
        this.atlasRepository = atlasRepository;
    }

    public List<TopicResponse> findAllReadable(AuthMember member) {
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return topicRepository.findAll().stream()
                .filter(member::canRead)
                .map(topic -> TopicResponse.from(topic, isInAtlas(topicsInAtlas, topic)))
                .toList();
    }

    private List<Topic> findTopicsInAtlas(AuthMember member) {
        return atlasRepository.findAllByMemberId(member.getMemberId())
                .stream()
                .map(Atlas::getTopic)
                .toList();
    }

    private boolean isInAtlas(List<Topic> topicsInAtlas, Topic topic) {
        return topicsInAtlas.contains(topic);
    }

    public TopicDetailResponse findDetailById(AuthMember member, Long id) {
        Topic topic = findTopic(id);
        validateReadableTopic(member, topic);
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return TopicDetailResponse.from(topic, isInAtlas(topicsInAtlas, topic));
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
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return topics.stream()
                .map(topic -> TopicDetailResponse.from(topic, isInAtlas(topicsInAtlas, topic)))
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
