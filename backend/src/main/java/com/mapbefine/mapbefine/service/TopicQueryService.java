package com.mapbefine.mapbefine.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapbefine.mapbefine.repository.TopicRepository;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

	private final TopicRepository topicRepository;

	public TopicQueryService(final TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

}
