package com.mapbefine.mapbefine.topic.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

    private final TopicRepository topicRepository;

    public TopicQueryService(final TopicRepository topicRepository, LocationRepository locationRepository) {
        this.topicRepository = topicRepository;
    }

    public List<TopicResponse> findAll(AuthMember member) {
        return topicRepository.findAll().stream()
                .filter(topic -> member.canRead(topic))
                .map(TopicResponse::from)
                .toList();
    }

    public List<TopicDetailResponse> findAllByIds(List<Long> ids) {
        return topicRepository.findByIdIn(ids).stream()
                .map(TopicDetailResponse::from)
                .collect(Collectors.toList());
    }

    public TopicDetailResponse findById(AuthMember member, Long id) {
        Topic topic = topicRepository.findById(id)
                .filter(member::canRead)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Topic이 존재하지 않습니다."));

        return TopicDetailResponse.from(topic);
    }
}
