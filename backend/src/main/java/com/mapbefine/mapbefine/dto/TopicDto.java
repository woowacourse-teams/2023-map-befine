package com.mapbefine.mapbefine.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;

public record TopicDto(
	Long id,
	String name,
	String description,
	List<Long> pins
) {
	public static TopicDto from(Topic topic) {
		List<Long> userPinIds = topic.getPins().stream()
			.map(Pin::getId)
			.collect(Collectors.toList());

		return new TopicDto(
			topic.getId(),
			topic.getName(),
			topic.getDescription(),
			userPinIds
		);
	}

}
