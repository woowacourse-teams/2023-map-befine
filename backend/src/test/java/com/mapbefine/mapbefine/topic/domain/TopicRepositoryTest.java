package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("토픽을 삭제하면, soft-deleting 된다.")
    void deleteById_Success() {
        //given
        Topic topic = Topic.createTopicAssociatedWithCreator(
                "토픽",
                "토픽설명",
                "https://example.com/image.jpg",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                member
        );

        topicRepository.save(topic);

        assertThat(topic.isDeleted()).isFalse();

        //when
        topicRepository.deleteById(topic.getId());

        //then
        Topic deletedTopic = topicRepository.findById(topic.getId()).get();
        assertThat(deletedTopic.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("특정 토픽의 즐겨찾기 여부를 함께 반환한다.")
    public void findWithBookmarkStatusByIdAndMemberId_Success1() {
        //given
        Topic topicNotBookmarked = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic bookmarkedTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topicNotBookmarked);
        topicRepository.save(bookmarkedTopic);

        //when
        TopicWithBookmarkStatus beforeBookmarking =
                topicRepository.findWithBookmarkStatusByIdAndMemberId(
                        bookmarkedTopic.getId(),
                        member.getId()
                ).get();
        assertThat(beforeBookmarking.getIsBookmarked()).isFalse();

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bookmarkedTopic, member);
        bookmarkRepository.save(bookmark);

        //then
        TopicWithBookmarkStatus afterBookmarking =
                topicRepository.findWithBookmarkStatusByIdAndMemberId(
                        bookmarkedTopic.getId(),
                        member.getId()).get();

        assertThat(afterBookmarking.getIsBookmarked()).isTrue();
    }

    @Test
    @DisplayName("다른 유저와 상관 없이, 특정 토픽의 즐겨찾기 여부를 함께 반환한다.")
    public void findWithBookmarkStatusByIdAndMemberId_Success2() {
        //given
        Member otherMember =
                MemberFixture.create("otherMember", "otherMember@naver.com", Role.USER);
        memberRepository.save(otherMember);

        Topic otherTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic bookmarkedTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(otherTopic);
        topicRepository.save(bookmarkedTopic);

        Bookmark otherBookmark =
                Bookmark.createWithAssociatedTopicAndMember(otherTopic, otherMember);
        bookmarkRepository.save(otherBookmark);

        //when
        TopicWithBookmarkStatus beforeBookmarking =
                topicRepository.findWithBookmarkStatusByIdAndMemberId(
                        bookmarkedTopic.getId(),
                        member.getId()
                ).get();
        assertThat(beforeBookmarking.getIsBookmarked()).isFalse();

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bookmarkedTopic, member);
        bookmarkRepository.save(bookmark);

        //then
        TopicWithBookmarkStatus afterBookmarking =
                topicRepository.findWithBookmarkStatusByIdAndMemberId(
                        bookmarkedTopic.getId(),
                        member.getId()).get();

        assertThat(afterBookmarking.getIsBookmarked()).isTrue();
    }

    @Test
    @DisplayName("각각의 토픽에 대한 즐겨찾기 여부를 함께 반환한다.")
    public void findAllWithBookmarkStatusByMemberId_Success1() {
        //given
        Topic topicNotBookmarked = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic bookmarkedTopic = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topicNotBookmarked);
        topicRepository.save(bookmarkedTopic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bookmarkedTopic, member);
        bookmarkRepository.save(bookmark);

        //when then
        List<TopicWithBookmarkStatus> topicsWithBookmarkStatus =
                topicRepository.findAllWithBookmarkStatusByMemberId(member.getId());

        assertThat(topicsWithBookmarkStatus).hasSize(2);
        assertThat(topicsWithBookmarkStatus).extractingResultOf("getIsBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("다른 유저와 상관없이, 해당 유저에 대한 각각의 토픽의 즐겨찾기 여부를 반환한다.")
    public void findAllWithBookmarkStatusByMemberId_Success2() {
        //given
        Member otherMember =
                MemberFixture.create("otherMember", "otherMember@naver.com", Role.USER);
        memberRepository.save(otherMember);

        Topic otherTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic bookmarkedTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(otherTopic);
        topicRepository.save(bookmarkedTopic);

        Bookmark otherBookmark =
                Bookmark.createWithAssociatedTopicAndMember(otherTopic, otherMember);
        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bookmarkedTopic, member);
        bookmarkRepository.save(otherBookmark);
        bookmarkRepository.save(bookmark);

        //when then
        List<TopicWithBookmarkStatus> topicsWithBookmarkStatus =
                topicRepository.findAllWithBookmarkStatusByMemberId(member.getId());

        assertThat(topicsWithBookmarkStatus).hasSize(2);
        assertThat(topicsWithBookmarkStatus).extractingResultOf("getIsBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("여러 토픽 조회시, 해당 유저에 대한 각각의 토픽의 즐겨찾기 여부를 반환한다.")
    public void findWithBookmarkStatusByIds_Success1() {
        //given
        Topic topicNotBookmarked = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic bookmarkedTopic = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topicNotBookmarked);
        topicRepository.save(bookmarkedTopic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bookmarkedTopic, member);
        bookmarkRepository.save(bookmark);

        //when then
        List<TopicWithBookmarkStatus> topicsWithBookmarkStatus =
                topicRepository.findWithBookmarkStatusByIdsAndMemberId(
                        List.of(topicNotBookmarked.getId(), bookmarkedTopic.getId()),
                        member.getId()
                );

        assertThat(topicsWithBookmarkStatus).hasSize(2);
        assertThat(topicsWithBookmarkStatus).extractingResultOf("getIsBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("여러 토픽 조회시, 다른 유저와 상관없이 해당 유저의 각각에 대한 토픽의 즐겨찾기 여부를 반환한다.")
    public void findWithBookmarkStatusByIds_Success2() {
        //given
        Member otherMember =
                MemberFixture.create("otherMember", "otherMember@naver.com", Role.USER);
        memberRepository.save(otherMember);

        Topic otherTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic bookmarkedTopic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(otherTopic);
        topicRepository.save(bookmarkedTopic);

        Bookmark otherBookmark =
                Bookmark.createWithAssociatedTopicAndMember(otherTopic, otherMember);
        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(bookmarkedTopic, member);
        bookmarkRepository.save(otherBookmark);
        bookmarkRepository.save(bookmark);

        //when then
        List<TopicWithBookmarkStatus> topicsWithBookmarkStatus =
                topicRepository.findWithBookmarkStatusByIdsAndMemberId(
                        List.of(otherTopic.getId(), bookmarkedTopic.getId()),
                        member.getId()
                );

        assertThat(topicsWithBookmarkStatus).hasSize(2);
        assertThat(topicsWithBookmarkStatus).extractingResultOf("getIsBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

}