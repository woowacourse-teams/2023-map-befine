package com.mapbefine.mapbefine.bookmark.application;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkId;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class BookmarkCommandServiceTest extends TestDatabaseContainer {

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
    @DisplayName("다른 회원의 토픽을 즐겨찾기에 추가할 수 있다.")
    void addTopicInBookmark_Success() {
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
        AuthMember user = MemberFixture.createUserWithoutTopics(otherMember);
        bookmarkCommandService.addTopicInBookmark(user, topic.getId());

        //then

        Bookmark bookmark = bookmarkRepository.findById(BookmarkId.of(topic.getId(), user.getMemberId())).get();

        assertThat(bookmark.getTopicId()).isEqualTo(topic.getId());
        assertThat(bookmark.getMemberId()).isEqualTo(otherMember.getId());
    }

    @Test
    @DisplayName("권한이 없는 다른 회원의 토픽을 즐겨찾기에 추가할 수 없다.")
    void addTopicInBookmark_Fail1() {
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
                MemberFixture.createUserWithoutTopics(otherMember),
                topic.getId()
        )).isInstanceOf(BookmarkForbiddenException.class);
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 토픽을 삭제할 수 있다.")
    void deleteTopicInBookmark_Success() {
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

        Bookmark bookmark = Bookmark.of(topic.getId(), otherMember.getId());
        bookmarkRepository.save(bookmark);

        //when
        AuthMember user = MemberFixture.createUserWithoutTopics(otherMember);
        assertThat(bookmarkRepository.existsById(bookmark.getId())).isTrue();

        bookmarkCommandService.deleteTopicInBookmark(user, topic.getId());

        //then
        assertThat(bookmarkRepository.existsById(bookmark.getId())).isFalse();
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 권한이 없는 토픽은 삭제할 수 없다.")
    void deleteTopicInBookmark_Fail() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.of(topic.getId(), creator.getId());
        bookmarkRepository.save(bookmark);

        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        //when then
        AuthMember otherUser = MemberFixture.createUserWithoutTopics(otherMember);

        assertThatThrownBy(() -> bookmarkCommandService.deleteTopicInBookmark(otherUser, topic.getId()))
                .isInstanceOf(BookmarkForbiddenException.class);
    }

}
