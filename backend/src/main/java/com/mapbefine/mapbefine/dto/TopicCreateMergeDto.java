package com.mapbefine.mapbefine.dto;

import java.util.List;

public record TopicCreateMergeDto(
	Long id,
	String name,
	String description,
	List<Long> topics
) {
	public static TopicCreateMergeDto from(TopicMergeRequest topicMergeRequest) {
		return new TopicCreateMergeDto(
			null,
			topicMergeRequest.name(),
			topicMergeRequest.description(),
			topicMergeRequest.topics()
		);
	}

}
