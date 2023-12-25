package com.mapbefine.mapbefine.bookmark.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkId;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkBadRequestException;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkConflictException;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkForbiddenException;
import com.mapbefine.mapbefine.topic.domain.BookmarkAdditionEvent;
import com.mapbefine.mapbefine.topic.domain.BookmarkDeleteEvent;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode.*;

@Service
@Transactional
public class BookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;

    private final TopicRepository topicRepository;

    private final ApplicationEventPublisher eventPublisher;

    public BookmarkCommandService(
            BookmarkRepository bookmarkRepository,
            TopicRepository topicRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.topicRepository = topicRepository;
        this.eventPublisher = eventPublisher;
    }

    public void addTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDuplication(authMember, topicId);
        Topic topic = getTopicById(topicId);
        validateBookmarkingPermission(authMember, topic);

        Bookmark bookmark = Bookmark.of(topic, authMember.getMemberId());
        bookmarkRepository.save(bookmark);
        eventPublisher.publishEvent(new BookmarkAdditionEvent(topicId));
    }

    private void validateBookmarkDuplication(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            throw new BookmarkConflictException(CONFLICT_TOPIC_ALREADY_ADD, topicId);
        }
    }

    private boolean isExistBookmark(AuthMember authMember, Long topicId) {
        return bookmarkRepository.existsById(BookmarkId.of(topicId, authMember.getMemberId()));
    }

    private Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new BookmarkBadRequestException(ILLEGAL_TOPIC_ID));
    }

    private void validateBookmarkingPermission(AuthMember authMember, Topic topic) {
        if (authMember.canRead(topic)) {
            return;
        }

        throw new BookmarkForbiddenException(FORBIDDEN_TOPIC_ADD);
    }

    public void deleteTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDeletingPermission(authMember, topicId);
        BookmarkId bookmarkId = BookmarkId.of(topicId, authMember.getMemberId());

        bookmarkRepository.deleteById(bookmarkId);
        eventPublisher.publishEvent(new BookmarkDeleteEvent(topicId));
    }

    private void validateBookmarkDeletingPermission(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            return;
        }

        throw new BookmarkForbiddenException(FORBIDDEN_TOPIC_DELETE);
    }

}
