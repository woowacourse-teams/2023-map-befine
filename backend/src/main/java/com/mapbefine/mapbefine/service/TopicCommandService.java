package com.mapbefine.mapbefine.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapbefine.mapbefine.dto.TopicCreateMergeDto;
import com.mapbefine.mapbefine.dto.TopicCreateNewDto;
import com.mapbefine.mapbefine.dto.TopicDto;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.entity.UserPin;
import com.mapbefine.mapbefine.repository.TopicRepository;
import com.mapbefine.mapbefine.repository.UserPinRepository;

@Transactional
@Service
public class TopicCommandService {

	private final TopicRepository topicRepository;
	private final UserPinRepository userPinRepository;

	public TopicCommandService(final TopicRepository topicRepository, final UserPinRepository userPinRepository) {
		this.topicRepository = topicRepository;
		this.userPinRepository = userPinRepository;
	}

	public TopicDto createNew(final TopicCreateNewDto topicCreateNewDto) {
		/// TODO 더 객체지향적으로 연관관계를 매핑할 수 없을까?
		Topic topic = new Topic(topicCreateNewDto.name(), topicCreateNewDto.description());
		topicRepository.save(topic);

		List<UserPin> original = userPinRepository.findAllById(topicCreateNewDto.pins());
		userPinRepository.saveAll(duplicateUserPins(original, topic));

		return TopicDto.from(topic);
	}

	private List<UserPin> duplicateUserPins(List<UserPin> userPins, Topic topic) {
		return userPins.stream()
			.map(original -> UserPin.duplicate(original, topic))
			.collect(Collectors.toList());
	}

	public TopicDto createMerge(TopicCreateMergeDto topicCreateMergeDto) {
		List<Topic> topics = topicRepository.findAllById(topicCreateMergeDto.topics());

		Topic topic = new Topic(topicCreateMergeDto.name(), topicCreateMergeDto.description());
		topicRepository.save(topic);

		List<UserPin> original = topics.stream()
			.map(Topic::getUserPins)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		List<UserPin> userPins = duplicateUserPins(original, topic);
		userPinRepository.saveAll(userPins);

		return TopicDto.from(topic);
	}

}
