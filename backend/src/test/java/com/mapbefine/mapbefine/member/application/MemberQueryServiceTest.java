package com.mapbefine.mapbefine.member.application;

import static com.mapbefine.mapbefine.member.domain.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
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
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberForbiddenException;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberQueryServiceTest {

    @Autowired
    private MemberQueryService memberQueryService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private AtlasRepository atlasRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private LocationRepository locationRepository;

    private AuthMember authMember;
    private Member member;
    private List<Topic> topics;


    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("member1", "member1@member.com", Role.ADMIN));
        authMember = new Admin(member.getId());

        createTopics(member);
        topics.forEach(topic -> atlasRepository.save(Atlas.createWithAssociatedMember(topic, member)));
        topics.forEach(topic -> bookmarkRepository.save(Bookmark.createWithAssociatedTopicAndMember(topic, member)));
    }

    private void createTopics(Member member) {
        topics = List.of(
                TopicFixture.createPublicAndAllMembersTopic(member),
                TopicFixture.createPublicAndAllMembersTopic(member),
                TopicFixture.createPublicAndAllMembersTopic(member)
        );
        topicRepository.saveAll(topics);
    }

    @Test
    @DisplayName("회원 목록을 조회한다.")
    void findAllMember() {
        // given
        Member member2 = memberRepository.save(
                MemberFixture.create("member2", "member2@member.com", USER)
        );
        Member member3 = memberRepository.save(
                MemberFixture.create("member3", "member3@member.com", USER)
        );

        // when
        List<MemberResponse> responses = memberQueryService.findAll();

        // then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(List.of(
                        MemberResponse.from(member),
                        MemberResponse.from(member2),
                        MemberResponse.from(member3)
                ));
    }

    @Test
    @DisplayName("회원을 단일 조회한다.")
    void findMemberById() {
        // given
        // when
        MemberDetailResponse response = memberQueryService.findById(authMember, member.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(MemberDetailResponse.from(member));
    }

    @Test
    @DisplayName("조회하려는 회원이 없는 경우 본인이 아니므로 예외를 반환한다.")
    void findMemberById_whenNoneExists_thenFail() {
        // given when then
        assertThatThrownBy(() -> memberQueryService.findById(authMember, Long.MAX_VALUE))
                .isInstanceOf(MemberForbiddenException.class);
    }

    @Test
    @DisplayName("조회하려는 회원이 본인이 아닌 경우 예외를 반환한다.")
    void findMemberById_whenNotSameMember_thenFail() {
        // given when then
        assertThatThrownBy(() -> memberQueryService.findById(authMember, Long.MAX_VALUE))
                .isInstanceOf(MemberForbiddenException.class);
    }

    @Test
    @DisplayName("즐겨찾기 목록에 추가 된 토픽을 조회할 수 있다")
    void findAllTopicsInBookmark_success() {
        // when
        List<TopicResponse> allTopicsInBookmark = memberQueryService.findAllTopicsInBookmark(authMember);

        // then
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        assertThat(allTopicsInBookmark).hasSize(topics.size());
        assertThat(allTopicsInBookmark).extractingResultOf("id")
                .isEqualTo(topicIds);
    }


    @Test
    @DisplayName("회원 ID를 이용해 모아보기할 모든 Topic들을 가져올 수 있다.")
    void findAtlasByMember_Success() {
        // when
        List<TopicResponse> allTopicsInAtlas = memberQueryService.findAllTopicsInAtlas(authMember);

        // then
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        assertThat(allTopicsInAtlas).hasSize(topics.size());
        assertThat(allTopicsInAtlas).extractingResultOf("id")
                .isEqualTo(topicIds);
    }

    @Test
    @DisplayName("로그인한 회원의 모든 지도를 가져올 수 있다.")
    void findMyAllTopics_Success() {
        //when
        List<TopicResponse> myAllTopics = memberQueryService.findMyAllTopics(authMember);

        //then
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        assertThat(myAllTopics).hasSize(topics.size());
        assertThat(myAllTopics).extractingResultOf("id")
                .isEqualTo(topicIds);
    }

    @Test
    @DisplayName("로그인한 회원의 모든 지도를 가져올 때, 삭제된 지도는 제외한다. (soft delete 반영)")
    void findMyAllTopics_Success_notContainingSoftDeleted() {
        // given
        List<Long> topicIds = topics.stream()
                .map(Topic::getId)
                .toList();

        // when
        Long deleted = topicIds.get(0);
        topicRepository.deleteById(deleted);
        topicRepository.flush();
        List<TopicResponse> myAllTopics = memberQueryService.findMyAllTopics(authMember);

        // then
        assertThat(myAllTopics).hasSize(topics.size() - 1);
        assertThat(myAllTopics).extractingResultOf("id")
                .doesNotContain(deleted);
    }

    @Test
    @DisplayName("로그인한 회원이 생성한 모든 핀을 가져올 수 있다.")
    void findMyAllPins_Success() {
        // given
        Location location = locationRepository.save(LocationFixture.create());

        List<Pin> expected = pinRepository.saveAll(List.of(
                PinFixture.create(location, topics.get(0), member),
                PinFixture.create(location, topics.get(1), member),
                PinFixture.create(location, topics.get(2), member)
        ));

        // when
        List<PinResponse> myAllPins = memberQueryService.findMyAllPins(authMember);

        //then
        List<Long> pinIds = expected.stream()
                .map(Pin::getId)
                .toList();

        assertThat(myAllPins).hasSize(expected.size());
        assertThat(myAllPins).extractingResultOf("id")
                .isEqualTo(pinIds);

    }

}
