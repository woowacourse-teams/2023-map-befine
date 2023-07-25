package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TopicCommandService {

    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;

    public TopicCommandService(final TopicRepository topicRepository, final PinRepository pinRepository) {
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
    }

    public long createNew(final TopicCreateRequest request) {
        Topic topic = new Topic(request.name(), request.description(), request.image());
        topicRepository.save(topic);

        List<Long> pinIds = request.pins();
        List<Pin> original = pinRepository.findAllById(pinIds);

        validateExist(pinIds.size(), original.size());
        pinRepository.saveAll(duplicateUserPins(original, topic));

        return topic.getId();
    }

    private void validateExist(int idCount, int existCount) {
        if (idCount != existCount) {
            throw new IllegalArgumentException("찾을 수 없는 ID가 포함되어 있습니다.");
        }
    }

    private List<Pin> duplicateUserPins(List<Pin> pins, Topic topic) {
        return pins.stream()
                .map(original -> original.copy(topic))
                .collect(Collectors.toList());
    }

    public long createMerge(TopicMergeRequest request) {
        List<Long> topicIds = request.topics();
        List<Topic> topics = topicRepository.findAllById(topicIds);

        validateExist(topicIds.size(), topics.size());

        Topic topic = new Topic(request.name(), request.description(), request.image());
        topicRepository.save(topic);

        List<Pin> original = topics.stream()
                .map(Topic::getPins)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Pin> pins = duplicateUserPins(original, topic);
        pinRepository.saveAll(pins);

        return topic.getId();
    }

    public void update(Long id, TopicUpdateRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));

        topic.update(request.name(), request.description());
    }

    public void delete(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));

        pinRepository.deleteAllByTopicId(id);
        topicRepository.deleteById(id);
    }

}
