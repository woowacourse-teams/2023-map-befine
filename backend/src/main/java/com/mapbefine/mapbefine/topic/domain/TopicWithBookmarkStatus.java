package com.mapbefine.mapbefine.topic.domain;

import lombok.Getter;

@Getter
public class TopicWithBookmarkStatus {

    private final Topic topic;
    private final Boolean isBookmarked;

    public TopicWithBookmarkStatus(Topic topic, Boolean isBookmarked) {
        this.topic = topic;
        this.isBookmarked = isBookmarked;
    }

}
