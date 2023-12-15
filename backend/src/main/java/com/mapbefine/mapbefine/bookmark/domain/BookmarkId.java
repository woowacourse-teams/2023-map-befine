package com.mapbefine.mapbefine.bookmark.domain;

import com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookmarkId implements Serializable {

    @Column(nullable = false, updatable = false)
    private Long topicId;

    @Column(nullable = false, updatable = false)
    private Long memberId;

    private BookmarkId(Long topicId, Long memberId) {
        this.topicId = topicId;
        this.memberId = memberId;
    }

    public static BookmarkId of(Long topicId, Long memberId) {
        validateNotNull(topicId, memberId);

        return new BookmarkId(topicId, memberId);
    }

    private static void validateNotNull(Long topicId, Long memberId) {
        if (Objects.isNull(topicId)) {
            throw new BookmarkBadRequestException(BookmarkErrorCode.ILLEGAL_TOPIC_ID);
        }

        if (Objects.isNull(memberId)) {
            throw new BookmarkBadRequestException(BookmarkErrorCode.ILLEGAL_MEMBER_ID);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkId that = (BookmarkId) o;
        return Objects.equals(getTopicId(), that.getTopicId()) && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopicId(), getMemberId());
    }
}
