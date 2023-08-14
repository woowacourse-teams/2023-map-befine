package com.mapbefine.mapbefine.topic.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
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
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TopicQueryServiceTest {

    @Autowired
    private TopicQueryService topicQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("읽을 수 있는 모든 Topic 목록을 조회한다.")
    void findAllReadable_Success1() {
        //given
        saveAllReadableTopicOfCount(1);
        saveOnlyMemberReadableTopicOfCount(2);

        AuthMember user = MemberFixture.createUser(member);

        //when
        List<TopicResponse> topics = topicQueryService.findAllReadable(user);

        //then
        assertThat(topics).hasSize(3);
        assertThat(topics).extractingResultOf("name")
                .containsExactlyInAnyOrder(
                        "아무나 읽을 수 있는 토픽",
                        "토픽 멤버만 읽을 수 있는 토픽",
                        "토픽 멤버만 읽을 수 있는 토픽"
                );
    }

    @Test
    @DisplayName("읽을 수 있는 모든 Topic 목록을 조회한다.")
    void findAllReadable_Success2() {
        //given
        saveAllReadableTopicOfCount(1);
        saveOnlyMemberReadableTopicOfCount(10);

        //when
        AuthMember guest = new Guest();

        List<TopicResponse> topics = topicQueryService.findAllReadable(guest);

        //then
        assertThat(topics).hasSize(1);
        assertThat(topics).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽");
    }


    void saveAllReadableTopicOfCount(int count) {
        for (int i = 0; i < count; i++) {
            topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        }
    }

    void saveOnlyMemberReadableTopicOfCount(int count) {
        for (int i = 0; i < count; i++) {
            topicRepository.save(TopicFixture.createPrivateAndGroupOnlyTopic(member));
        }
    }

    @Test
    @DisplayName("권한이 있는 토픽을 ID로 조회한다.")
    void findDetailById_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic);

        //when
        AuthMember authMember = new Admin(member.getId());

        TopicDetailResponse detail = topicQueryService.findDetailById(authMember, topic.getId());

        //then
        assertThat(detail.id()).isEqualTo(topic.getId());
        assertThat(detail.name()).isEqualTo("아무나 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 없는 토픽을 ID로 조회하면, 예외가 발생한다.")
    void findDetailById_Fail() {
        //given
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(member);

        topicRepository.save(topic);

        //when then
        AuthMember guest = new Guest();

        assertThatThrownBy(() -> topicQueryService.findDetailById(guest, topic.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조회권한이 없는 Topic 입니다.");
    }

    @Test
    @DisplayName("권한이 있는 토픽을 여러개의 ID로 조회한다.")
    void findDetailByIds_Success1() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember guest = new Guest();

        List<TopicDetailResponse> details = topicQueryService.findDetailsByIds(
                guest,
                List.of(topic1.getId(), topic2.getId())
        );

        //then
        assertThat(details).hasSize(2);
        assertThat(details).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽", "아무나 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 있는 토픽을 여러개의 ID로 조회한다.")
    void findDetailByIds_Success2() {
        //given
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember user = MemberFixture.createUser(member);

        List<TopicDetailResponse> details = topicQueryService.findDetailsByIds(
                user,
                List.of(topic1.getId(), topic2.getId())
        );

        //then
        assertThat(details).hasSize(2);
        assertThat(details).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽", "토픽 멤버만 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 없는 토픽을 여러개의 ID로 조회하면, 예외가 발생한다.")
    void findDetailByIds_Fail1() {
        //given
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when then
        AuthMember guest = new Guest();
        List<Long> topicIds = List.of(topic1.getId(), topic2.getId());

        assertThatThrownBy(() -> topicQueryService.findDetailsByIds(guest, topicIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("읽을 수 없는 토픽이 존재합니다.");
    }

    @Test
    @DisplayName("조회하려는 토픽 중 존재하지 않는 ID가 존재하면, 예외가 발생한다.")
    void findDetailByIds_Fail2() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when then
        AuthMember user = MemberFixture.createUser(member);
        List<Long> topicIds = List.of(topic1.getId(), topic2.getId(), Long.MAX_VALUE);

        assertThatThrownBy(() -> topicQueryService.findDetailsByIds(user, topicIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 토픽이 존재합니다");
    }

    @Test
    @DisplayName("모든 토픽을 조회할 때, 즐겨찾기 여부를 함께 반환한다.")
    public void findAllReadableWithBookmark_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when //then
        AuthMember user = MemberFixture.createUser(member);
        List<TopicResponse> topics = topicQueryService.findAllReadable(user);

        assertThat(topics).hasSize(2);
        assertThat(topics).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topics).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("모든 토픽을 조회할 때, 로그인 유저가 아니면 즐겨찾기 여부가 항상 False다")
    public void findAllReadableWithoutBookmark_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when
        //then
        AuthMember guest = new Guest();
        List<TopicResponse> topics = topicQueryService.findAllReadable(guest);

        assertThat(topics).hasSize(2);
        assertThat(topics).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topics).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.FALSE);
    }

    @Test
    @DisplayName("토픽 상세조회시, 즐겨찾기 여부를 함께 반환한다.")
    public void findWithBookmarkStatus_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        bookmarkRepository.save(bookmark);

        //when then
        AuthMember user = MemberFixture.createUser(member);
        TopicDetailResponse topicDetail = topicQueryService.findDetailById(user, topic.getId());

        assertThat(topicDetail.id()).isEqualTo(topic.getId());
        assertThat(topicDetail.isBookmarked()).isEqualTo(Boolean.TRUE);

    }

    @Test
    @DisplayName("토픽 상세조회시, 로그인 유저가 아니라면 즐겨찾기 여부가 항상 False다.")
    public void findWithoutBookmarkStatus_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        bookmarkRepository.save(bookmark);

        //when then
        AuthMember guest = new Guest();
        TopicDetailResponse topicDetail = topicQueryService.findDetailById(guest, topic.getId());

        assertThat(topicDetail.id()).isEqualTo(topic.getId());
        assertThat(topicDetail.isBookmarked()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("여러 토픽 조회시, 즐겨 찾기 여부를 함께 반환한다.")
    public void findDetailsWithBookmarkStatus_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when //then
        AuthMember user = MemberFixture.createUser(member);
        List<TopicDetailResponse> topicDetails =
                topicQueryService.findDetailsByIds(user, List.of(topic1.getId(), topic2.getId()));

        assertThat(topicDetails).hasSize(2);
        assertThat(topicDetails).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topicDetails).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("여러 토픽 조회시, 로그인 유저가 아니라면 즐겨 찾기 여부가 항상 False다.")
    public void findDetailsWithoutBookmarkStatus_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when //then
        AuthMember guest = new Guest();
        List<TopicDetailResponse> topicDetails =
                topicQueryService.findDetailsByIds(guest, List.of(topic1.getId(), topic2.getId()));

        assertThat(topicDetails).hasSize(2);
        assertThat(topicDetails).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topicDetails).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.FALSE);
    }

    @Test
    @DisplayName("멤버 Id를 이용하여 그 멤버가 만든 모든 Topic을 확인할 수 있다.")
    void findAllTopicsByMemberId_Success() {
        //given
        AuthMember authMember = new Admin(member.getId());

        List<Topic> expected = topicRepository.saveAll(List.of(
                TopicFixture.createPublicAndAllMembersTopic(member),
                TopicFixture.createPublicAndAllMembersTopic(member),
                TopicFixture.createPublicAndAllMembersTopic(member)
        ));

        //when
        List<TopicResponse> actual = topicQueryService.findAllTopicsByMemberId(authMember, member.getId());

        //then
        List<Long> topicIds = expected.stream()
                .map(Topic::getId)
                .toList();
        assertThat(actual).hasSize(expected.size());
        assertThat(actual).extractingResultOf("id")
                .isEqualTo(topicIds);
    }

}
