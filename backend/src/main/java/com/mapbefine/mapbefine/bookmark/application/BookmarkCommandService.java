package com.mapbefine.mapbefine.bookmark.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkId;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkBadRequestException;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkConflictException;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkForbiddenException;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode.*;

@Service
@Transactional
public class BookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;

    private final TopicRepository topicRepository;

    public BookmarkCommandService(
            BookmarkRepository bookmarkRepository,
            TopicRepository topicRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.topicRepository = topicRepository;
    }

    public void addTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDuplication(authMember, topicId);
        Topic topic = getTopicById(topicId);
        validateBookmarkingPermission(authMember, topic);

        Bookmark bookmark = Bookmark.of(topic, authMember.getMemberId());
        bookmarkRepository.save(bookmark);
        topic.increaseBookmarkCount();
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

    // TODO: 2023/12/03 BookmarkCount의 정합성을 어떻게 맞출 것인가 ? 매번 topic 조회하는 것은 불필요한 행위같아보임
    public void deleteTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDeletingPermission(authMember, topicId);
        BookmarkId bookmarkId = BookmarkId.of(topicId, authMember.getMemberId());

        bookmarkRepository.deleteById(bookmarkId);
        Topic topic = getTopicById(topicId);
        topic.decreaseBookmarkCount();
    }

    private void validateBookmarkDeletingPermission(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            return;
        }

        throw new BookmarkForbiddenException(FORBIDDEN_TOPIC_DELETE);
    }

}
