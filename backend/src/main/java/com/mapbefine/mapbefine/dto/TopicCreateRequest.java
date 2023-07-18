package com.mapbefine.mapbefine.dto;

import java.util.List;

public record TopicCreateRequest(
	String name,
	String description,
	List<Long> pins
) {
}
