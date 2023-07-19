package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.TopicDetailResponse;
import com.mapbefine.mapbefine.dto.TopicResponse;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

    private final TopicRepository topicRepository;

    public TopicQueryService(final TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<TopicResponse> findAll() {
        return topicRepository.findAll().stream()
                .map(TopicResponse::from)
                .collect(Collectors.toList());
    }

    public TopicDetailResponse findById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Topic이 존재하지 않습니다."));

        return TopicDetailResponse.from(topic);
    }
}
