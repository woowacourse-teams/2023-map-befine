package com.mapbefine.mapbefine.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapbefine.mapbefine.dto.TopicCreateMergeDto;
import com.mapbefine.mapbefine.dto.TopicCreateNewDto;
import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicDto;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.service.TopicCommandService;
import com.mapbefine.mapbefine.service.TopicQueryService;

@RestController
@RequestMapping("/topics")
public class TopicController {

	private final TopicCommandService topicCommandService;
	private final TopicQueryService topicQueryService;

	public TopicController(final TopicCommandService topicCommandService, final TopicQueryService topicQueryService) {
		this.topicCommandService = topicCommandService;
		this.topicQueryService = topicQueryService;
	}

	@PostMapping("/new")
	public ResponseEntity<Void> createNew(@RequestBody TopicCreateRequest topicCreateRequest) {
		TopicDto topic = topicCommandService.createNew(TopicCreateNewDto.from(topicCreateRequest));

		return ResponseEntity.created(URI.create("/topics/" + topic.id())).build();
	}

	@PostMapping("/merge")
	public ResponseEntity<Void> createMerge(@RequestBody TopicMergeRequest topicMergeRequest) {
		TopicDto topic = topicCommandService.createMerge(TopicCreateMergeDto.from(topicMergeRequest));

		return ResponseEntity.created(URI.create("/topics/" + topic.id())).build();
	}

}
