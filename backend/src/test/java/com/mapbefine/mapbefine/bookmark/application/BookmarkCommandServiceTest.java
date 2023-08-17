package com.mapbefine.mapbefine.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.exception.BookmarkException.BookmarkForbiddenException;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@ServiceTest
class BookmarkCommandServiceTest {

    @Autowired
    private BookmarkCommandService bookmarkCommandService;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("다른 유저의 토픽을 즐겨찾기에 추가할 수 있다.")
    public void addTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        //when
        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);
        Long bookmarkId = bookmarkCommandService.addTopicInBookmark(
                MemberFixture.createUser(otherMember),
                topic.getId()
        );

        //then
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).get();

        assertThat(bookmark.getTopic().getId()).isEqualTo(topic.getId());
        assertThat(bookmark.getMember().getId()).isEqualTo(otherMember.getId());
    }

    @Test
    @DisplayName("권한이 없는 다른 유저의 토픽을 즐겨찾기에 추가할 수 없다.")
    public void addTopicInBookmark_Fail1() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        //when
        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        //then
        assertThatThrownBy(() -> bookmarkCommandService.addTopicInBookmark(
                MemberFixture.createUser(otherMember),
                topic.getId()
        )).isInstanceOf(BookmarkForbiddenException.class);
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 토픽을 삭제할 수 있다.")
    public void deleteTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, otherMember);
        bookmarkRepository.save(bookmark);

        //when
        AuthMember user = MemberFixture.createUser(otherMember);
        assertThat(bookmarkRepository.existsById(bookmark.getId())).isTrue();

        bookmarkCommandService.deleteTopicInBookmark(user, topic.getId());

        //then
        assertThat(bookmarkRepository.existsById(bookmark.getId())).isFalse();
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 권한이 없는 토픽은 삭제할 수 없다.")
    public void deleteTopicInBookmark_Fail() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, creator);
        bookmarkRepository.save(bookmark);

        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        //when then
        AuthMember otherUser = MemberFixture.createUser(otherMember);

        assertThatThrownBy(() -> bookmarkCommandService.deleteTopicInBookmark(otherUser, topic.getId()))
                .isInstanceOf(BookmarkForbiddenException.class);
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 모든 토픽을 삭제할 수 있다")
    public void deleteAllBookmarks_Success() {
        //given
        Member creatorBefore = memberRepository.save(MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        ));
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(creatorBefore);
        Topic topic2 = TopicFixture.createPrivateAndGroupOnlyTopic(creatorBefore);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark1 = Bookmark.createWithAssociatedTopicAndMember(topic1, creatorBefore);
        Bookmark bookmark2 = Bookmark.createWithAssociatedTopicAndMember(topic1, creatorBefore);

        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        //when
        assertThat(creatorBefore.getBookmarks()).hasSize(2);

        AuthMember user = MemberFixture.createUser(creatorBefore);
        bookmarkCommandService.deleteAllBookmarks(user);

        testEntityManager.flush();
        testEntityManager.clear();

        //then
        Member creatorAfter = memberRepository.findById(creatorBefore.getId()).get();
        assertThat(creatorAfter.getBookmarks()).hasSize(0);
    }

}
