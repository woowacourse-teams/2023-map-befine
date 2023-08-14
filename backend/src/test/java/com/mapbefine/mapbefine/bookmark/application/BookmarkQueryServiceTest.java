package com.mapbefine.mapbefine.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class BookmarkQueryServiceTest {

    @Autowired
    private BookmarkQueryService bookmarkQueryService;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("즐겨찾기 목록에 추가 된 토픽을 조회할 수 있다")
    public void findAllTopicsInBookmark_success() {
        //given
        Member creator = MemberFixture.create("creator", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic1 = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(creator));
        Topic topic2 = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(creator));
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        Member otherMember =
                MemberFixture.create("otherMember", "otherMember@naver.com", Role.USER);
        memberRepository.save(otherMember);
        Bookmark bookmark1 =
                Bookmark.createWithAssociatedTopicAndMember(topic1, otherMember);
        Bookmark bookmark2 =
                Bookmark.createWithAssociatedTopicAndMember(topic2, otherMember);

        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        //then
        List<TopicResponse> topicsInBookmark = bookmarkQueryService.findAllTopicsInBookmark(
                MemberFixture.createUser(otherMember)
        );

        assertThat(topicsInBookmark).hasSize(2);
        assertThat(topicsInBookmark).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());

    }
}