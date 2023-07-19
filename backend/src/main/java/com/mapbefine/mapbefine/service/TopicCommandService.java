package com.mapbefine.mapbefine.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;

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
		Topic topic = new Topic(request.name(), request.description());
		topicRepository.save(topic);

		// TODO id 유효성 검증 or SQL Exception 전환
		List<Pin> original = pinRepository.findAllById(request.pins());
		pinRepository.saveAll(duplicateUserPins(original, topic));

		return topic.getId();
	}

	private List<Pin> duplicateUserPins(List<Pin> pins, Topic topic) {
		return pins.stream()
			.map(original -> original.duplicate(topic))
			.collect(Collectors.toList());
	}

	public long createMerge(TopicMergeRequest request) {
		// TODO id 유효성 검증 or SQL Exception 전환
		List<Topic> topics = topicRepository.findAllById(request.topics());

		Topic topic = new Topic(request.name(), request.description());
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
		// TODO id 유효성 검증 or SQL Exception 전환
		Topic topic = topicRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));

		topic.update(request.name(), request.description());
	}

	public void delete(Long id) {
		// TODO id 유효성 검증 or SQL Exception 전환
		Topic topic = topicRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));

		pinRepository.deleteAllByTopicId(id);
		topic.delete();
	}

}
