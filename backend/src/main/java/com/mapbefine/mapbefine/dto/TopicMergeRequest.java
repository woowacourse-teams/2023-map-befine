package com.mapbefine.mapbefine.dto;

import java.util.List;

public record TopicMergeRequest(
	String name,
	String description,
	List<Long> topics
) {
}
