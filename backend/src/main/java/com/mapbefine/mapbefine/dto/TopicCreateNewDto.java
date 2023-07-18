package com.mapbefine.mapbefine.dto;

import java.util.List;

public record TopicCreateNewDto(
	Long id,
	String name,
	String description,
	List<Long> pins
) {
	public static TopicCreateNewDto from(TopicCreateRequest topicCreateRequest) {
		return new TopicCreateNewDto(
			null,
			topicCreateRequest.name(),
			topicCreateRequest.description(),
			topicCreateRequest.pins()
		);
	}

}
