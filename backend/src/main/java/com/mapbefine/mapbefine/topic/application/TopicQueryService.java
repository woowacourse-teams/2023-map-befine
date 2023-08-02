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

    public List<TopicResponse> findAll(AuthMember member) {
        return topicRepository.findAll().stream()
                .filter(member::canRead)
                .map(TopicResponse::from)
                .toList();
    }

    public List<TopicDetailResponse> findDetailsByIds(List<Long> ids) {
        return topicRepository.findByIdIn(ids).stream()
                .map(TopicDetailResponse::from)
                .toList();
    }

    public TopicDetailResponse findDetailById(AuthMember member, Long id) {
        Topic topic = topicRepository.findById(id)
                .filter(member::canRead)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Topic이 존재하지 않습니다."));

        return TopicDetailResponse.from(topic);
    }

}
