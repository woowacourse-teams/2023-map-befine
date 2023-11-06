package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.member.MemberFixture.createUser;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Cluster;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.ClusterResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicForbiddenException;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TopicQueryServiceTest extends TestDatabaseContainer {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TopicQueryService topicQueryService;

    private Member member;

    @BeforeEach
    void setup() {
        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("비로그인 회원이 읽을 수 있는 모든 Topic 목록을 조회한다.")
    void findAllReadable_Guest_Success() {
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

    @Test
    @DisplayName("로그인 회원이 읽을 수 있는 모든 Topic 목록을 조회한다.")
    void findAllReadable_User_Success() {
        //given
        saveAllReadableTopicOfCount(1);
        saveOnlyMemberReadableTopicOfCount(2);

        AuthMember user = createUser(member);

        //when
        List<TopicResponse> topics = topicQueryService.findAllReadable(user);

        //then
        assertThat(topics).hasSize(3);
        assertThat(topics).extractingResultOf("name")
                .containsExactlyInAnyOrder(
                        "아무나 읽을 수 있는 토픽",
                        "토픽 회원만 읽을 수 있는 토픽",
                        "토픽 회원만 읽을 수 있는 토픽"
                );
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
    @DisplayName("Topic 목록 조회 시 삭제된 것은 제외하고 조회한다. (soft delete 반영)")
    void findAllReadable_IsDeletedFalse_Success() {
        //given
        saveAllReadableTopicOfCount(1);
        saveSoftDeletedTopicOfCount(3);

        //when
        AuthMember guest = new Guest();

        List<TopicResponse> topics = topicQueryService.findAllReadable(guest);

        //then
        assertThat(topics).hasSize(1);
        assertThat(topics).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽");
    }

    void saveSoftDeletedTopicOfCount(int count) {
        for (int i = 0; i < count; i++) {
            Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
            topicRepository.save(topic);
            topicRepository.deleteById(topic.getId());
        }
    }

    @Test
    @DisplayName("모든 토픽을 조회할 때, 삭제된 토픽은 볼 수 없다. (soft delete 반영)")
    void findAllReadable_WithoutSoftDeleted_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);
        topicRepository.deleteById(topic2.getId());

        //when //then
        AuthMember guest = new Guest();
        List<TopicResponse> topics = topicQueryService.findAllReadable(guest);

        assertThat(topics).hasSize(1);
        assertThat(topics).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId());
    }

    @Test
    @DisplayName("모든 토픽을 조회할 때, 즐겨찾기 여부를 함께 반환한다.")
    void findAllReadableWithBookmark_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when then
        AuthMember user = createUser(member);
        List<TopicResponse> topics = topicQueryService.findAllReadable(user);

        assertThat(topics).hasSize(2);
        assertThat(topics).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topics).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("모든 토픽을 조회할 때, 로그인 회원이 아니면 즐겨찾기 여부가 항상 False다")
    void findAllReadableWithoutBookmark_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when //then
        AuthMember guest = new Guest();
        List<TopicResponse> topics = topicQueryService.findAllReadable(guest);

        assertThat(topics).hasSize(2);
        assertThat(topics).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topics).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.FALSE);
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
    @DisplayName("토픽 상세 조회 시 삭제된 핀은 볼 수 없다. (soft delete 반영)")
    void findDetailById_Success_getPinsOnlyNotDeleted() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        Location location = LocationFixture.create();
        Pin pin = PinFixture.create(location, topic, member);
        locationRepository.save(location);
        topicRepository.save(topic);
        pinRepository.save(pin);
        pinRepository.deleteById(pin.getId());

        //when
        TopicDetailResponse response = topicQueryService.findDetailById(new Admin(member.getId()), topic.getId());

        //then
        assertThat(response.pins()).isEmpty();
    }

    @Test
    @DisplayName("토픽 상세 조회 시 토픽의 변경일자는 핀의 최신 변경 일자이다.")
    void findDetailById_Success_lastPinUpdatedAt() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        Location location = LocationFixture.create();
        Pin pin = PinFixture.create(location, topic, member);
        locationRepository.save(location);
        topicRepository.save(topic);
        pinRepository.save(pin);

        //when
        pin.updatePinInfo("updatePin", "updatedAt will be update");
        pinRepository.flush();
        TopicDetailResponse response = topicQueryService.findDetailById(new Admin(member.getId()), topic.getId());

        //then
        assertThat(response.updatedAt()).isEqualTo(pin.getUpdatedAt());
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
                .isInstanceOf(TopicForbiddenException.class);
    }

    @Test
    @DisplayName("토픽 상세조회시, 삭제된 토픽은 볼 수 없다. (soft delete 반영)")
    void findDetailById_WithOutSoftDeleted_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic);
        topicRepository.deleteById(topic.getId());

        //when then
        AuthMember user = createUser(member);
        assertThatThrownBy(() -> topicQueryService.findDetailById(user, topic.getId()))
                .isInstanceOf(TopicNotFoundException.class);
    }

    @Test
    @DisplayName("토픽 상세조회시, 즐겨찾기 여부, 모아보기 여부, 수정 권한 여부를 함께 반환한다.")
    void findDetailById_WithBookmarkStatus_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        bookmarkRepository.save(bookmark);

        //when then
        AuthMember user = createUser(member);
        TopicDetailResponse topicDetail = topicQueryService.findDetailById(user, topic.getId());

        assertThat(topicDetail.id()).isEqualTo(topic.getId());
        assertThat(topicDetail.isBookmarked()).isEqualTo(Boolean.TRUE);
        assertThat(topicDetail.isInAtlas()).isEqualTo(Boolean.FALSE);
        assertThat(topicDetail.canUpdate()).isEqualTo(Boolean.TRUE);
    }

    @Test
    @DisplayName("토픽 상세조회시, 로그인 회원이 아니라면 즐겨찾기 여부, 모아보기 여부, 수정 권한 여부가 항상 False다.")
    void findDetailById_WithoutBookmarkStatus_Success() {
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
        assertThat(topicDetail.isInAtlas()).isEqualTo(Boolean.FALSE);
        assertThat(topicDetail.canUpdate()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("비로그인 회원이 권한이 있는 토픽을 여러개의 ID로 조회할 수 있다.")
    void findDetailsByIds_Guest_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember guest = new Guest();

        List<TopicDetailResponse> details = topicQueryService.findDetailsByIds(
                guest,
                of(topic1.getId(), topic2.getId())
        );

        //then
        assertThat(details).hasSize(2);
        assertThat(details).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽", "아무나 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("로그인 회원이 권한이 있는 토픽을 여러개의 ID로 조회할 수 있다.")
    void findDetailsByIds_User_Success() {
        //given
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember user = createUser(member);

        List<TopicDetailResponse> details = topicQueryService.findDetailsByIds(
                user,
                of(topic1.getId(), topic2.getId())
        );

        //then
        assertThat(details).hasSize(2);
        assertThat(details).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽", "토픽 회원만 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 없는 토픽을 여러개의 ID로 조회하면, 예외가 발생한다.")
    void findDetailsByIds_FailByForbidden() {
        //given
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when then
        AuthMember guest = new Guest();
        List<Long> topicIds = of(topic1.getId(), topic2.getId());

        assertThatThrownBy(() -> topicQueryService.findDetailsByIds(guest, topicIds))
                .isInstanceOf(TopicForbiddenException.class);
    }

    @Test
    @DisplayName("조회하려는 토픽 중 존재하지 않는 ID가 존재하면, 예외가 발생한다. (soft delete 반영)")
    void findDetailsByIds_FailByNotFound() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);
        topicRepository.deleteById(topic2.getId());

        //when then
        AuthMember user = createUser(member);
        List<Long> topicIds = of(topic1.getId(), topic2.getId());

        assertThatThrownBy(() -> topicQueryService.findDetailsByIds(user, topicIds))
                .isInstanceOf(TopicNotFoundException.class);
    }

    @Test
    @DisplayName("여러 토픽 조회시, 즐겨 찾기 여부를 함께 반환한다.")
    void findDetailsByIds_WithBookmarkStatus_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic1, member);
        bookmarkRepository.save(bookmark);

        //when //then
        AuthMember user = createUser(member);
        List<TopicDetailResponse> topicDetails =
                topicQueryService.findDetailsByIds(user, of(topic1.getId(), topic2.getId()));

        assertThat(topicDetails).hasSize(2);
        assertThat(topicDetails).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topicDetails).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.TRUE);
    }

    @Test
    @DisplayName("여러 토픽 조회시, 로그인 회원이 아니라면 즐겨 찾기 여부가 항상 False다.")
    void findDetailsByIds_WithoutBookmarkStatus_Success() {
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
                topicQueryService.findDetailsByIds(guest, of(topic1.getId(), topic2.getId()));

        assertThat(topicDetails).hasSize(2);
        assertThat(topicDetails).extractingResultOf("id")
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
        assertThat(topicDetails).extractingResultOf("isBookmarked")
                .containsExactlyInAnyOrder(Boolean.FALSE, Boolean.FALSE);
    }

    @Test
    @DisplayName("회원 Id를 이용하여 그 회원이 만든 모든 Topic을 확인할 수 있다.")
    void findAllTopicsByMemberId_Success() {
        //given
        AuthMember authMember = new Admin(member.getId());

        List<Topic> expected = topicRepository.saveAll(of(
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

    @Test
    @DisplayName("핀 수정일 기준으로 토픽을 나열한다")
    void findAllByOrderByUpdatedAtDesc_Success() {
        // given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        List<Topic> topics = of(
                TopicFixture.createByName("5등", member),
                TopicFixture.createByName("4등", member),
                TopicFixture.createByName("3등", member),
                TopicFixture.createByName("2등", member),
                TopicFixture.createByName("1등", member)

        );
        topicRepository.saveAll(topics);

        List<Pin> pins = topics.stream()
                .map(topic -> PinFixture.create(location, topic, member))
                .toList();
        pinRepository.saveAll(pins);

        User user = new User(member.getId(), Collections.emptyList(), Collections.emptyList());

        // when
        List<TopicResponse> responses = topicQueryService.findAllByOrderByUpdatedAtDesc(user);

        // then
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("즐겨찾기가 많이 있는 토픽 순서대로 조회할 수 있다.")
    void findAllBestTopics_Success1() {
        //given
        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@email.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        Topic topicWithNoBookmark = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topicWithOneBookmark = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topicWithTwoBookmark = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topicWithNoBookmark);
        topicRepository.save(topicWithOneBookmark);
        topicRepository.save(topicWithTwoBookmark);

        saveBookmark(topicWithOneBookmark, member);
        saveBookmark(topicWithTwoBookmark, member);
        saveBookmark(topicWithTwoBookmark, otherMember);

        //when
        AuthMember user = createUser(member);
        List<TopicResponse> actual = topicQueryService.findAllBestTopics(user);

        //then
        assertThat(actual).extractingResultOf("id")
                .containsExactly(
                        topicWithTwoBookmark.getId(),
                        topicWithOneBookmark.getId(),
                        topicWithNoBookmark.getId()
                );
    }

    @Test
    @DisplayName("즐겨찾기 순서대로 조회하더라도, private 토픽인 경우 조회할 수 없다.")
    void findAllBestTopics_Success2() {
        //given
        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@email.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        Topic topicWithNoBookmark = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic privateTopicWithOneBookmark = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        topicRepository.save(topicWithNoBookmark);
        topicRepository.save(privateTopicWithOneBookmark);

        saveBookmark(privateTopicWithOneBookmark, member);

        //when
        AuthMember otherUser = createUser(otherMember);

        List<TopicResponse> actual = topicQueryService.findAllBestTopics(otherUser);
        List<TopicResponse> expect = topicQueryService.findAllReadable(otherUser);

        //then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("여러 지도를 한번에 렌더링 할 때, 클러스터링 된 결과를 반환받는다.")
    void findClusteringPinsByIds_success() {
        // given
        Topic firstTopic = topicRepository.save(TopicFixture.createByName("firstTopic", member));
        Topic secondTopic = topicRepository.save(TopicFixture.createByName("secondTopic", member));
        Pin representPinOfSet1 = pinRepository.save(PinFixture.create(
                locationRepository.save(LocationFixture.createByCoordinate(36, 124)), firstTopic, member)
        );
        Pin pinOfSet1 = pinRepository.save(PinFixture.create(
                locationRepository.save(LocationFixture.createByCoordinate(36, 124.1)), secondTopic, member)
        );
        Pin representPinOfSet2 = pinRepository.save(PinFixture.create(
                locationRepository.save(LocationFixture.createByCoordinate(36, 124.2)), firstTopic, member)
        );
        Pin representPinOfSet3 = pinRepository.save(PinFixture.create(
                locationRepository.save(LocationFixture.createByCoordinate(38, 124)), firstTopic, member)
        );
        Pin pinOfSet3 = pinRepository.save(PinFixture.create(
                locationRepository.save(LocationFixture.createByCoordinate(38, 124.1)), secondTopic, member)
        );

        // when
        List<ClusterResponse> actual = topicQueryService.findClusteringPinsByIds(
                createUser(member),
                of(firstTopic.getId(), secondTopic.getId()),
                9000
        );


        // then
        List<ClusterResponse> expected = List.of(
                ClusterResponse.from(Cluster.from(representPinOfSet1, List.of(representPinOfSet1, pinOfSet1))),
                ClusterResponse.from(Cluster.from(representPinOfSet2, List.of(representPinOfSet2))),
                ClusterResponse.from(Cluster.from(representPinOfSet3, List.of(representPinOfSet3, pinOfSet3)))
        );
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("여러 지도를 한번에 렌더링 할 떄, 조회하지 못하는 지도가 있어 예외가 발생한다.")
    void findClusteringPinsByIds_fail() {
        // given
        Member nonCreator = memberRepository.save(MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.USER));
        Member creator = memberRepository.save(MemberFixture.create("creator", "creator@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createPrivateAndGroupOnlyTopic(creator));

        // when then
        assertThatThrownBy(() -> topicQueryService.findClusteringPinsByIds(
                createUser(nonCreator),
                List.of(topic.getId()),
                9000)
        ).isInstanceOf(TopicForbiddenException.class);
    }

    private Bookmark saveBookmark(Topic topic, Member member) {
        return bookmarkRepository.save(Bookmark.createWithAssociatedTopicAndMember(topic, member));
    }

}
