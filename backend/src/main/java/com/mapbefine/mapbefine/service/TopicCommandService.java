package com.mapbefine.mapbefine.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapbefine.mapbefine.dto.TopicCreateMergeDto;
import com.mapbefine.mapbefine.dto.TopicCreateNewDto;
import com.mapbefine.mapbefine.dto.TopicDto;
import com.mapbefine.mapbefine.dto.TopicUpdateDto;
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

	public TopicDto createNew(final TopicCreateNewDto topicCreateNewDto) {
		/// TODO 더 객체지향적으로 연관관계를 매핑할 수 없을까?
		Topic topic = new Topic(topicCreateNewDto.name(), topicCreateNewDto.description());
		topicRepository.save(topic);

		List<Pin> original = pinRepository.findAllById(topicCreateNewDto.pins());
		pinRepository.saveAll(duplicateUserPins(original, topic));

		return TopicDto.from(topic);
	}

	private List<Pin> duplicateUserPins(List<Pin> pins, Topic topic) {
		return pins.stream()
			.map(original -> original.duplicate(topic))
			.collect(Collectors.toList());
	}

	public TopicDto createMerge(TopicCreateMergeDto topicCreateMergeDto) {
		List<Topic> topics = topicRepository.findAllById(topicCreateMergeDto.topics());

		Topic topic = new Topic(topicCreateMergeDto.name(), topicCreateMergeDto.description());
		topicRepository.save(topic);

		List<Pin> original = topics.stream()
			.map(Topic::getPins)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		List<Pin> pins = duplicateUserPins(original, topic);
		pinRepository.saveAll(pins);

		return TopicDto.from(topic);
	}

	public void update(TopicUpdateDto topicUpdateDto) {
		Topic topic = topicRepository.findById(topicUpdateDto.id())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));

		topic.update(topicUpdateDto.name(), topicUpdateDto.description());
	}

	public void delete(Long id) {
		Topic topic = topicRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));

		// TODO: 연관된 UserPin Cascade로 삭제하도록 변경
		List<Long> pinIds = topic.getPins()
			.stream()
			.map(Pin::getId)
			.collect(Collectors.toList());
		pinRepository.deleteAllById(pinIds);

		topicRepository.delete(topic);
	}
}
