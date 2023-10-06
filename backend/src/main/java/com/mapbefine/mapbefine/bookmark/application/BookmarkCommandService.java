package com.mapbefine.mapbefine.bookmark.application;

import static com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode.CONFLICT_TOPIC_ALREADY_ADD;
import static com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode.FORBIDDEN_TOPIC_ADD;
import static com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode.FORBIDDEN_TOPIC_DELETE;
import static com.mapbefine.mapbefine.bookmark.exception.BookmarkErrorCode.ILLEGAL_TOPIC_ID;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkBadRequestException;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkConflictException;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkForbiddenException;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookmarkCommandService {

    private final BookmarkRepository bookmarkRepository;

    private final MemberRepository memberRepository;

    private final TopicRepository topicRepository;

    public BookmarkCommandService(
            BookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            TopicRepository topicRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
    }

    public Long addTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDuplication(authMember, topicId);
        Topic topic = getTopicById(topicId);
        validateBookmarkingPermission(authMember, topic);
        Member member = findMemberById(authMember);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        bookmarkRepository.save(bookmark);

        return bookmark.getId();
    }

    private Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new BookmarkBadRequestException(ILLEGAL_TOPIC_ID));
    }

    private void validateBookmarkDuplication(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            throw new BookmarkConflictException(CONFLICT_TOPIC_ALREADY_ADD, topicId);
        }
    }

    private boolean isExistBookmark(AuthMember authMember, Long topicId) {
        return bookmarkRepository.existsByMemberIdAndTopicId(authMember.getMemberId(), topicId);
    }

    private void validateBookmarkingPermission(AuthMember authMember, Topic topic) {
        if (authMember.canRead(topic)) {
            return;
        }

        throw new BookmarkForbiddenException(FORBIDDEN_TOPIC_ADD);
    }

    private Member findMemberById(AuthMember authMember) {
        Long memberId = authMember.getMemberId();

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("findMemberById; memberId=" + memberId));
    }

    public void deleteTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDeletingPermission(authMember, topicId);

        Bookmark bookmark = findBookmarkByMemberIdAndTopicId(authMember.getMemberId(), topicId);
        Topic topic = getTopicById(topicId);

        topic.removeBookmark(bookmark);
    }

    private Bookmark findBookmarkByMemberIdAndTopicId(Long memberId, Long topicId) {
        return bookmarkRepository.findByMemberIdAndTopicId(memberId, topicId)
                .orElseThrow(() -> new NoSuchElementException(
                        "findBookmarkByMemberIdAndTopicId; memberId=" + memberId + " topicId=" + topicId
                ));
    }

    private void validateBookmarkDeletingPermission(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            return;
        }

        throw new BookmarkForbiddenException(FORBIDDEN_TOPIC_DELETE);
    }

    @Deprecated
    public void deleteAllBookmarks(AuthMember authMember) {
        bookmarkRepository.deleteAllByMemberId(authMember.getMemberId());
    }

}
