package com.mapbefine.mapbefine.bookmark.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    @MapsId("topicId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    private Bookmark(BookmarkId id, Topic topic) {
        this.id = id;
        this.topic = topic;
    }

    public static Bookmark of(Topic topic, Long memberId) {
        return new Bookmark(BookmarkId.of(topic.getId(), memberId), topic);
    }

    public Long getTopicId() {
        return id.getTopicId();
    }

    public Long getMemberId() {
        return id.getMemberId();
    }

}
