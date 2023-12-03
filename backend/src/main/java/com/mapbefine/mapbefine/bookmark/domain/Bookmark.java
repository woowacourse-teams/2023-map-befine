package com.mapbefine.mapbefine.bookmark.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    private Bookmark(BookmarkId id) {
        this.id = id;
    }

    public static Bookmark of(Long topicId, Long memberId) {
        return new Bookmark(BookmarkId.of(topicId, memberId));
    }

    public Long getTopicId() {
        return id.getTopicId();
    }

    public Long getMemberId() {
        return id.getMemberId();
    }

}
