package com.mapbefine.mapbefine.pin.application;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.pin.PinCommentFixture;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.PinImageFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import com.mapbefine.mapbefine.pin.domain.PinCommentRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinCommentResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinNotFoundException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.mapbefine.mapbefine.topic.domain.PermissionType.ALL_MEMBERS;
import static com.mapbefine.mapbefine.topic.domain.PermissionType.GROUP_ONLY;
import static com.mapbefine.mapbefine.topic.domain.Publicity.PRIVATE;
import static com.mapbefine.mapbefine.topic.domain.Publicity.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class PinQueryServiceTest extends TestDatabaseContainer {

    @Autowired
    private PinQueryService pinQueryService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PinCommentRepository pinCommentRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    private Location location;
    private Topic publicUser1Topic;
    private Topic privateUser2Topic;
    private Member user1;
    private Member user2;
    private AuthMember authMemberUser1;

    @BeforeEach
    void setUp() {
        location = locationRepository.save(LocationFixture.create());
        user1 = memberRepository.save(MemberFixture.create("user1", "userfirst@naver.com", Role.USER));
        user2 = memberRepository.save(MemberFixture.create("user2", "usersecond@naver.com", Role.USER));
        publicUser1Topic = topicRepository.save(TopicFixture.createByName("topic", user1));
        privateUser2Topic = topicRepository.save(TopicFixture.createPrivateByName("private", user2));

        List<Long> topicIdsWithPermissions = permissionRepository.findAllTopicIdsByMemberId(user1.getId());
        List<Long> createdTopicIds = user1.getCreatedTopics()
                .stream()
                .map(Topic::getId)
                .toList();
        authMemberUser1 = new User(user1.getId(), createdTopicIds, topicIdsWithPermissions);
    }

    @Test
    @DisplayName("현재 권한에서 조회 가능한 핀 목록을 가져온다.")
    void findAllReadable_Success() {
        // given
        Pin notExpected = pinRepository.save(PinFixture.create(location, privateUser2Topic, user2));

        List<PinResponse> expected = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Pin pin = PinFixture.create(location, publicUser1Topic, user1);
            pinRepository.save(pin);
            expected.add(PinResponse.from(pin));
        }

        // when
        List<PinResponse> actual = pinQueryService.findAllReadable(authMemberUser1);

        // then
        assertThat(actual).doesNotContain(PinResponse.from(notExpected));
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("현재 권한에서 조회 가능한 핀 목록을 가져올 때 삭제된 핀은 제외한다. (soft delete 반영)")
    void findAllReadable_WithOutSoftDeleted_Success() {
        // given
        Pin notExpected = PinFixture.create(location, privateUser2Topic, user2);
        pinRepository.save(notExpected);
        pinRepository.deleteById(notExpected.getId());

        Pin expected = PinFixture.create(location, publicUser1Topic, user1);
        pinRepository.save(expected);

        // when
        List<PinResponse> actual = pinQueryService.findAllReadable(authMemberUser1);

        // then
        assertThat(actual).doesNotContain(PinResponse.from(notExpected))
                .containsOnly(PinResponse.from(expected));
    }

    @Test
    @DisplayName("핀의 상세 정보를 조회할 수 있다.")
    void findDetailById_Success() {
        // given
        Pin pin = PinFixture.create(location, publicUser1Topic, user1);
        PinImageFixture.create(pin);
        pinRepository.save(pin);
        Long pinId = pin.getId();

        // when
        PinDetailResponse actual = pinQueryService.findDetailById(authMemberUser1, pinId);

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(PinDetailResponse.of(pin, Boolean.TRUE));
    }

    @Test
    @DisplayName("존재하지 않는 핀의 Id로 조회 시 예외를 발생시킨다. (soft delete 반영)")
    void findDetailById_FailByNonExisting() {
        // given
        Pin pin = pinRepository.save(PinFixture.create(location, publicUser1Topic, user1));
        Long softDeletedPin = pin.getId();
        pinRepository.deleteById(softDeletedPin);

        // when, then
        assertThatThrownBy(() -> pinQueryService.findDetailById(authMemberUser1, softDeletedPin))
                .isInstanceOf(PinNotFoundException.class);
    }

    @Test
    @DisplayName("현재 회원의 조회 권한이 없는 핀의 상세 정보 조회 시 예외를 발생시킨다.")
    void findDetailById_FailByForbidden() {
        // given
        Pin pin = pinRepository.save(PinFixture.create(location, privateUser2Topic, user2));

        // when, then
        assertThatThrownBy(() -> pinQueryService.findDetailById(authMemberUser1, pin.getId()))
                .isInstanceOf(PinForbiddenException.class);
    }

    @Test
    @DisplayName("현재 삭제된 핀의 상세 정보 조회 시 예외를 발생시킨다. (soft delete 반영)")
    void findDetailById_FailByNotFound() {
        // given
        Pin pin = pinRepository.save(PinFixture.create(location, publicUser1Topic, user1));
        pinRepository.deleteById(pin.getId());

        // when, then
        assertThatThrownBy(() -> pinQueryService.findDetailById(authMemberUser1, pin.getId()))
                .isInstanceOf(PinNotFoundException.class);
    }

    @Test
    @DisplayName("회원 Id를 이용하여 그 회원이 만든 모든 Pin을 확인할 수 있다.")
    void findAllPinsByMemberId_success() {
        // given
        List<Pin> expected = pinRepository.saveAll(List.of(
                PinFixture.create(location, publicUser1Topic, user1),
                PinFixture.create(location, publicUser1Topic, user1),
                PinFixture.create(location, publicUser1Topic, user1)
        ));

        // when
        List<PinResponse> actual = pinQueryService.findAllPinsByMemberId(authMemberUser1, user1.getId());

        // then
        List<Long> pinIds = expected.stream()
                .map(Pin::getId)
                .toList();

        assertThat(actual).hasSize(expected.size());
        assertThat(actual).extractingResultOf("id")
                .isEqualTo(pinIds);
    }

    @ParameterizedTest
    @MethodSource("publicTopicsStatus")
    @DisplayName("공개 지도인 경우, Guest 는 핀 댓글을 조회에 성공한다.")
    void findAllPinCommentGuest_Success(Publicity publicity, PermissionType permissionType) {
        // given
        Topic topic = TopicFixture.createByPublicityAndPermissionTypeAndCreator(publicity, permissionType, user1);
        Topic savedTopic = topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, savedTopic, user1));
        PinComment savedPinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user1));
        PinCommentResponse expected = PinCommentResponse.of(savedPinComment, false);

        // when
        List<PinCommentResponse> actual = pinQueryService.findAllPinCommentsByPinId(new Guest(), savedPin.getId());

        // then
        assertThat(actual.get(0)).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("비공개 지도인 경우, Guest 는 핀 댓글을 조회를 할 수 없다.")
    void findAllPinCommentGuest_Fail() {
        // given
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(user1);
        Topic savedTopic = topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, savedTopic, user1));

        // when then
        assertThatThrownBy(() -> pinQueryService.findAllPinCommentsByPinId(new Guest(), savedPin.getId()))
                .isInstanceOf(PinForbiddenException.class);
    }

    @ParameterizedTest
    @MethodSource("publicAndPrivateTopicsStatus")
    @DisplayName("일반 회원은 공개 지도인 경우와, 비공개 지도이면서 본인이 권한을 가진 지도의 핀 댓글을 조회할 수 있다.")
    void findAllPinCommentUser_Success(Publicity publicity, PermissionType permissionType) {
        // given
        Topic topic = TopicFixture.createByPublicityAndPermissionTypeAndCreator(publicity, permissionType, user1);
        Topic savedTopic = topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, savedTopic, user1));
        PinComment savedPinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user1));
        PinCommentResponse expected = PinCommentResponse.of(savedPinComment, true);
        AuthMember creatorUser = MemberFixture.createUserWithoutTopics(user1);

        // when
        List<PinCommentResponse> actual = pinQueryService.findAllPinCommentsByPinId(creatorUser, savedPin.getId());

        // then
        assertThat(actual.get(0)).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("일반 회원인 경우 비공개 지도이면서 권한을 가지지 않은 지도에 핀 댓글을 조회할 수 없다.")
    void findAllPinCommentUser_Fail() {
        // given
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(user1);
        Topic savedTopic = topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, savedTopic, user1));
        pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user1));
        AuthMember nonCreatorUser = MemberFixture.createUserWithoutTopics(user2);

        // when then
        assertThatThrownBy(() -> pinQueryService.findAllPinCommentsByPinId(nonCreatorUser, savedPin.getId()))
                .isInstanceOf(PinForbiddenException.class);
    }

    @ParameterizedTest
    @MethodSource("publicAndPrivateTopicsStatus")
    @DisplayName("ADMIN 은 어떠한 유형의 지도의 핀 댓글을 조회할 수 있다.")
    void findAllPinCommentAdmin_Success(Publicity publicity, PermissionType permissionType) {
        // given
        Topic topic = TopicFixture.createByPublicityAndPermissionTypeAndCreator(publicity, permissionType, user1);
        Topic savedTopic = topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, savedTopic, user1));
        PinComment savedPinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user1));
        PinCommentResponse expected = PinCommentResponse.of(savedPinComment, true);
        Member nonCreator = memberRepository.save(
                MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.ADMIN)
        );
        AuthMember nonCreatorAdmin = MemberFixture.createUserWithoutTopics(nonCreator);

        // when
        List<PinCommentResponse> actual = pinQueryService.findAllPinCommentsByPinId(nonCreatorAdmin, savedPin.getId());

        // then
        assertThat(actual.get(0)).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    static Stream<Arguments> publicTopicsStatus() {
        return Stream.of(
                Arguments.of(PUBLIC, ALL_MEMBERS),
                Arguments.of(PUBLIC, GROUP_ONLY)
        );
    }

    static Stream<Arguments> publicAndPrivateTopicsStatus() {
        return Stream.of(
                Arguments.of(PUBLIC, ALL_MEMBERS),
                Arguments.of(PUBLIC, GROUP_ONLY),
                Arguments.of(PRIVATE, GROUP_ONLY)
        );
    }

}
