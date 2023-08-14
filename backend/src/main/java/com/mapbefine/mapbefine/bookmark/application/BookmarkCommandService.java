package com.mapbefine.mapbefine.bookmark.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        Member member = getMemberById(authMember);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        bookmarkRepository.save(bookmark);

        return bookmark.getId();
    }

    private Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 토픽입니다."));
    }

    private void validateBookmarkDuplication(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            throw new IllegalArgumentException("이미 즐겨찾기로 등록된 토픽입니다.");
        }
    }

    private boolean isExistBookmark(AuthMember authMember, Long topicId) {
        return bookmarkRepository.existsByMemberIdAndTopicId(authMember.getMemberId(), topicId);
    }

    private void validateBookmarkingPermission(AuthMember authMember, Topic topic) {
        if (authMember.canRead(topic)) {
            return;
        }

        throw new IllegalArgumentException("토픽에 대한 권한이 없어서 즐겨찾기에 추가할 수 없습니다.");
    }

    private Member getMemberById(AuthMember authMember) {
        return memberRepository.findById(authMember.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버입니다."));
    }

    public void deleteTopicInBookmark(AuthMember authMember, Long topicId) {
        validateBookmarkDeletingPermission(authMember, topicId);

        bookmarkRepository.deleteByMemberIdAndTopicId(authMember.getMemberId(), topicId);
    }

    private void validateBookmarkDeletingPermission(AuthMember authMember, Long topicId) {
        if (isExistBookmark(authMember, topicId)) {
            return;
        }

        throw new IllegalArgumentException("즐겨찾기 삭제에 대한 권한이 없습니다.");
    }

    public void deleteAllBookmarks(AuthMember authMember) {
        bookmarkRepository.deleteAllByMemberId(authMember.getMemberId());
    }

}
