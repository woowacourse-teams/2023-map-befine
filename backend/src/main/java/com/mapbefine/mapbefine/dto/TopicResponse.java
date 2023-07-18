package com.mapbefine.mapbefine.dto;

import java.time.LocalDateTime;

import com.mapbefine.mapbefine.entity.Topic;

public record TopicResponse(
	long id,
	String name,
	String description,
	int pinCount,
	LocalDateTime updatedAt
) {
	public static TopicResponse from(Topic topic) {
		return new TopicResponse(
			topic.getId(),
			topic.getName(),
			topic.getDescription(),
			topic.countPins(),
			topic.getUpdatedAt()
		);
	}
}
