package com.mapbefine.mapbefine.pin.application;

import static com.mapbefine.mapbefine.topic.domain.PermissionType.ALL_MEMBERS;
import static com.mapbefine.mapbefine.topic.domain.PermissionType.GROUP_ONLY;
import static com.mapbefine.mapbefine.topic.domain.Publicity.PRIVATE;
import static com.mapbefine.mapbefine.topic.domain.Publicity.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.image.FileFixture;
import com.mapbefine.mapbefine.image.exception.ImageException.ImageBadRequestException;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinCommentFixture;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import com.mapbefine.mapbefine.pin.domain.PinCommentRepository;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCommentCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinCommentUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.pin.exception.PinCommentException.PinCommentBadRequestException;
import com.mapbefine.mapbefine.pin.exception.PinCommentException.PinCommentForbiddenException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@ServiceTest
class PinCommandServiceTest extends TestDatabaseContainer {

    private static final MultipartFile BASE_IMAGE_FILE = FileFixture.createFile();
    private static final String BASE_IMAGE = "https://mapbefine.github.io/favicon.png";

    @Autowired
    private PinCommandService pinCommandService;
    @Autowired
    private PinQueryService pinQueryService;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PinImageRepository pinImageRepository;
    @Autowired
    private PinCommentRepository pinCommentRepository;

    private Location location;
    private Topic topic;
    private Member user;
    private Member member;
    private AuthMember authMember;
    private PinCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("user1", "userfirst@naver.com", Role.ADMIN));
        user = memberRepository.save(MemberFixture.create("user2", "usersecond@naver.com", Role.USER));

        location = locationRepository.save(LocationFixture.create());
        topic = topicRepository.save(TopicFixture.createByName("topic", member));

        authMember = new Admin(member.getId());
        createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                location.getRoadBaseAddress(),
                location.getLegalDongCode(),
                location.getLatitude(),
                location.getLongitude()
        );
    }

    @Test
    @DisplayName("핀을 저장하려는 위치(Location)가 존재하면 해당 위치에 핀을 저장한다.")
    void saveIfExistLocation_Success() {
        // given, when
        long savedPinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, savedPinId);
        Pin pin = pinRepository.findById(savedPinId).get();
        Location savedLocation = pin.getLocation();

        // then
        assertThat(savedLocation.getId()).isEqualTo(location.getId());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(PinDetailResponse.of(pin, Boolean.TRUE));
    }

    @Test
    @DisplayName("핀을 저장하려는 존재하지 않는 위치(Location)가 존재하지 않으면 위치를 저장하고 핀을 저장한다.")
    void saveIfNotExistLocation_Success() {
        // given
        double newLatitude = 37.123456;
        double newLongitude = 127.123456;

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                location.getRoadBaseAddress(),
                location.getLegalDongCode(),
                newLatitude,
                newLongitude
        );

        // when
        long savedPinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, savedPinId);
        Pin pin = pinRepository.findById(savedPinId).get();
        Location savedLocation = pin.getLocation();

        // then
        assertThat(savedLocation.getId()).isNotEqualTo(location.getId());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(PinDetailResponse.of(pin, Boolean.TRUE));

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(2);
        assertThat(locations)
                .extractingResultOf("getId")
                .containsExactly(location.getId(), savedLocation.getId());
    }

    @Test
    @DisplayName("핀을 추가하면 토픽에 핀의 변경 일시를 새로 반영한다. (모든 일시는 영속화 시점 기준이다.)")
    void save_Success_UpdateLastPinAddedAt() {
        // given when
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);
        Pin pin = pinRepository.findById(pinId)
                .orElseGet(Assertions::fail);

        // then
        topicRepository.findById(createRequest.topicId())
                .ifPresentOrElse(
                        topic -> assertThat(topic.getLastPinUpdatedAt()).isEqualTo(pin.getCreatedAt()),
                        Assertions::fail
                );
    }

    @Test
    @DisplayName("권한이 없는 토픽에 핀을 저장하면 예외를 발생시킨다.")
    void save_FailByForbidden() {
        assertThatThrownBy(() -> pinCommandService.save(new Guest(), List.of(BASE_IMAGE_FILE), createRequest))
                .isInstanceOf(PinForbiddenException.class);
    }


    @Test
    @DisplayName("핀을 변경하면 토픽에 핀의 변경 일시를 새로 반영한다. (모든 일시는 영속화 시점 기준이다.)")
    void update_Success_UpdateLastPinsAddedAt() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        // when
        pinCommandService.update(authMember, pinId, new PinUpdateRequest("name", "update"));
        Pin pin = pinRepository.findById(pinId)
                .orElseGet(Assertions::fail);
        pinRepository.flush();

        // then
        topicRepository.findById(createRequest.topicId())
                .ifPresentOrElse(
                        topic -> assertThat(topic.getLastPinUpdatedAt()).isEqualTo(pin.getUpdatedAt()),
                        Assertions::fail
                );
    }

    @Test
    @DisplayName("권한이 없는 토픽에 핀을 수정하면 예외를 발생시킨다.")
    void update_FailByForbidden() {
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        assertThatThrownBy(() -> pinCommandService.update(new Guest(), pinId, new PinUpdateRequest("name", "update")))
                .isInstanceOf(PinForbiddenException.class);
    }

    @Test
    @DisplayName("핀을 삭제하면 해당 핀을 soft delete 하고, 해당 핀의 이미지들도 soft delete 한다.")
    void removeById_Success() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);
        Pin pin = pinRepository.findById(pinId).get();
        PinImage.createPinImageAssociatedWithPin(BASE_IMAGE, pin);

        // when
        pinCommandService.removeById(authMember, pinId);

        // then
        assertThat(pinRepository.findById(pinId))
                .isEmpty();
        assertThat(pinImageRepository.findById(pinId))
                .isEmpty();
    }

    @Test
    @DisplayName("권한이 없는 토픽의 핀을 삭제하면 예외를 발생시킨다.")
    void removeById_FailByForbidden() {
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        assertThatThrownBy(() -> pinCommandService.removeById(new Guest(), pinId))
                .isInstanceOf(PinForbiddenException.class);
    }

    @Test
    @DisplayName("핀 id를 전달받아 해당하는 핀에 핀 이미지를 추가한다.")
    void addImage_Success() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        // when
        long pinImageId = savePinImageAndGetId(pinId);

        // then
        pinImageRepository.findById(pinImageId)
                .ifPresentOrElse(
                        found -> assertThat(found.getImageUrl()).isNotNull(),
                        Assertions::fail
                );
    }

    @Test
    @DisplayName("이미지가 null 인 경우 예외를 발생시킨다.")
    void addImage_FailByNull() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        PinImageCreateRequest imageNullRequest = new PinImageCreateRequest(pinId, null);

        // when then
        assertThatThrownBy(() -> pinCommandService.addImage(authMember, imageNullRequest))
                .isInstanceOf(ImageBadRequestException.class);
    }

    @Test
    @DisplayName("권한이 없는 토픽의 핀에 핀 이미지를 추가하면 예외를 발생시킨다.")
    void addImage_FailByForbidden() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        // when, then
        assertThatThrownBy(() -> pinCommandService.addImage(new Guest(), new PinImageCreateRequest(pinId,
                BASE_IMAGE_FILE)))
                .isInstanceOf(PinForbiddenException.class);
    }

    @Test
    @DisplayName("핀 이미지의 id를 전달받아 해당하는 핀 이미지를 soft delete 한다.")
    void removeImageById_Success() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);
        long pinImageId = savePinImageAndGetId(pinId);

        // when
        pinCommandService.removeImageById(authMember, pinImageId);

        // then
        assertThat(pinImageRepository.findById(pinImageId))
                .isEmpty();
    }

    private long savePinImageAndGetId(long pinId) {
        pinCommandService.addImage(authMember, new PinImageCreateRequest(pinId, BASE_IMAGE_FILE));
        List<PinImageResponse> pinImages = pinQueryService.findDetailById(authMember, pinId)
                .images();
        PinImageResponse pinImage = pinImages.get(0);
        return pinImage.id();
    }

    @Test
    @DisplayName("권한이 없는 토픽의 핀 이미지를 삭제하면 예외를 발생시킨다.")
    void removeImageById_FailByForbidden() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);
        long pinImageId = savePinImageAndGetId(pinId);

        // when, then
        assertThatThrownBy(() -> pinCommandService.removeImageById(new Guest(), pinImageId))
                .isInstanceOf(PinForbiddenException.class);
    }

    @Test
    @DisplayName("Guest 인 경우 핀 댓글을 생성하면 예외가 발생된다.")
    void savePinComment_Fail_ByGuest() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinCommentCreateRequest request = new PinCommentCreateRequest(savedPin.getId(), null, "댓글");

        // when then
        assertThatThrownBy(() -> pinCommandService.savePinComment(new Guest(), request))
                .isInstanceOf(PinCommentForbiddenException.class);
    }

    @ParameterizedTest
    @MethodSource("publicAndPrivateTopicsStatus")
    @DisplayName("일반 회원인 경우 공개 지도, 비공개 지도이지만 권한을 가진 지도에는 핀 댓글을 생성할 수 있다.")
    void savePinComment_Success_ByCreator(Publicity publicity, PermissionType permissionType) {
        // given
        Topic topic = TopicFixture.createByPublicityAndPermissionTypeAndCreator(publicity, permissionType, user);
        topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinCommentCreateRequest request = new PinCommentCreateRequest(
                savedPin.getId(), null, "댓글"
        );
        AuthMember creatorUser = MemberFixture.createUser(user);

        // when
        Long pinCommentId = pinCommandService.savePinComment(creatorUser, request);

        // then
        PinComment actual = pinCommentRepository.findById(pinCommentId).get();
        PinComment expected = PinComment.ofParentPinComment(savedPin, user, "댓글");

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("일반 회원인 경우 비공개 지도이면서 권한을 가지고 있지 않은 지도에 핀 댓글을 생성할 수 없다.")
    void savePinComment_Fail_ByNonCreator() {
        // given
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(user);
        topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinCommentCreateRequest request = new PinCommentCreateRequest(
                savedPin.getId(), null, "댓글"
        );
        Member nonCreator = memberRepository.save(
                MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.USER)
        );
        AuthMember nonCreatorUser = MemberFixture.createUser(nonCreator);

        // when then
        assertThatThrownBy(() -> pinCommandService.savePinComment(nonCreatorUser, request))
                .isInstanceOf(PinCommentForbiddenException.class);
    }

    @Test
    @DisplayName("핀 대댓글에는 대댓글을 달 수 없다. (depth 2 이상)")
    void savePinComment_Fail_ByIllegalDepth() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment parentPinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        PinComment childPinComment = pinCommentRepository.save(
                PinCommentFixture.createChildComment(savedPin, user, parentPinComment)
        );
        PinCommentCreateRequest request = new PinCommentCreateRequest(
                savedPin.getId(), childPinComment.getId(), "대대댓글"
        );
        AuthMember creatorUser = MemberFixture.createUser(user);

        // when then
        assertThatThrownBy(() -> pinCommandService.savePinComment(creatorUser, request))
                .isInstanceOf(PinCommentBadRequestException.class);
    }

    @ParameterizedTest
    @MethodSource("publicAndPrivateTopicsStatus")
    @DisplayName("Admin 인 경우 어떠한 유형의 지도라도 핀 댓글을 생성할 수 있다.")
    void savePinComment_Success_ByAdmin(Publicity publicity, PermissionType permissionType) {
        // given
        Topic topic = TopicFixture.createByPublicityAndPermissionTypeAndCreator(publicity, permissionType, user);
        topicRepository.save(topic);
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinCommentCreateRequest request = new PinCommentCreateRequest(
                savedPin.getId(), null, "댓글"
        );
        Member nonCreator = memberRepository.save(
                MemberFixture.create("admin", "admin@naver.com", Role.ADMIN)
        );
        AuthMember nonCreatorAdmin = MemberFixture.createUser(nonCreator);

        // when
        Long pinCommentId = pinCommandService.savePinComment(nonCreatorAdmin, request);

        // then
        PinComment actual = pinCommentRepository.findById(pinCommentId).get();
        PinComment expected = PinComment.ofParentPinComment(savedPin, nonCreator, "댓글");
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Guest 인 경우 핀 댓글을 수정할 수 없다.")
    void updatePinComment_Fail_ByGuest() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        PinCommentUpdateRequest request = new PinCommentUpdateRequest(
                "댓글 수정!"
        );

        // when then
        assertThatThrownBy(() -> pinCommandService.updatePinComment(new Guest(), pinComment.getId(), request))
                .isInstanceOf(PinCommentForbiddenException.class);
    }

    @Test
    @DisplayName("일반 회원인 경우 본인이 단 핀 댓글을 수정할 수 있다.")
    void updatePinComment_Success_ByCreator() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        PinCommentUpdateRequest request = new PinCommentUpdateRequest(
                "댓글 수정!"
        );
        AuthMember creatorUser = MemberFixture.createUser(user);

        // when
        pinCommandService.updatePinComment(creatorUser, pinComment.getId(), request);

        // then
        PinComment actual = pinCommentRepository.findById(pinComment.getId()).get();
        PinComment expected = PinComment.ofParentPinComment(savedPin, user, "댓글 수정!");
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("일반 회원인 경우 본인이 달지 않은 핀 댓글을 수정할 수 없다.")
    void updatePinComment_Fail_ByNonCreator() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        PinCommentUpdateRequest request = new PinCommentUpdateRequest(
                "댓글 수정!"
        );
        Member nonCreator = memberRepository.save(
                MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.USER)
        );
        AuthMember nonCreatorUser = MemberFixture.createUser(nonCreator);

        // when then
        assertThatThrownBy(() -> pinCommandService.updatePinComment(nonCreatorUser, pinComment.getId(), request))
                .isInstanceOf(PinCommentForbiddenException.class);
    }

    @Test
    @DisplayName("Admin 인 경우 본인이 단 핀 댓글을 수정할 수 있다.")
    void updatePinComment_Success_ByAdmin() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        PinCommentUpdateRequest request = new PinCommentUpdateRequest(
                "댓글 수정!"
        );
        AuthMember creatorAdmin = new Admin(user.getId());

        // when
        pinCommandService.updatePinComment(creatorAdmin, pinComment.getId(), request);

        // then
        PinComment actual = pinCommentRepository.findById(pinComment.getId()).get();
        PinComment expected = PinComment.ofParentPinComment(savedPin, user, "댓글 수정!");
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Admin 인 경우 본인이 달지 않은 핀 댓글을 수정할 수 있다.")
    void updatePinComment_Success_ByNonCreatorAdmin() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        PinCommentUpdateRequest request = new PinCommentUpdateRequest(
                "댓글 수정!"
        );
        Member nonCreator = memberRepository.save(
                MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.ADMIN)
        );
        AuthMember nonCreatorAdmin = MemberFixture.createUser(nonCreator);

        // when
        pinCommandService.updatePinComment(nonCreatorAdmin, pinComment.getId(), request);

        // then
        PinComment actual = pinCommentRepository.findById(pinComment.getId()).get();
        PinComment expected = PinComment.ofParentPinComment(savedPin, user, "댓글 수정!");
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Guest 인 경우 핀 댓글을 삭제할 수 없다.")
    void deletePinComment_Fail_ByGuest() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));

        // when then
        assertThatThrownBy(() -> pinCommandService.deletePinComment(new Guest(), pinComment.getId()))
                .isInstanceOf(PinCommentForbiddenException.class);
    }

    @Test
    @DisplayName("일반 회원인 경우 본인이 단 핀 댓글을 삭제할 수 있다.")
    void deletePinComment_Success_ByCreator() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        AuthMember creatorUser = MemberFixture.createUser(user);

        // when
        pinCommandService.deletePinComment(creatorUser, pinComment.getId());

        // then
        assertThat(pinCommentRepository.existsById(pinComment.getId())).isFalse();
    }

    @Test
    @DisplayName("일반 회원인 경우 본인이 달지 않은 핀 댓글을 삭제할 수 없다.")
    void deletePinComment_Fail_ByNonCreator() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        Member nonCreator = memberRepository.save(
                MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.USER)
        );
        AuthMember nonCreatorUser = MemberFixture.createUser(nonCreator);

        // when then
        assertThatThrownBy(() -> pinCommandService.deletePinComment(nonCreatorUser, pinComment.getId()))
                .isInstanceOf(PinCommentForbiddenException.class);
    }

    @Test
    @DisplayName("Admin 인 경우 본인이 단 핀 댓글을 삭제할 수 있다.")
    void deletePinComment_Success_ByAdmin() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        AuthMember creatorAdmin = new Admin(user.getId());

        // when
        pinCommandService.deletePinComment(creatorAdmin, pinComment.getId());

        // then
        assertThat(pinCommentRepository.existsById(pinComment.getId())).isFalse();
    }

    @Test
    @DisplayName("Admin 인 경우 본인이 달지 않은 핀 댓글을 삭제할 수 있다.")
    void deletePinComment_Success_ByNonCreatorAdmin() {
        // given
        Pin savedPin = pinRepository.save(PinFixture.create(location, topic, user));
        PinComment pinComment = pinCommentRepository.save(PinCommentFixture.createParentComment(savedPin, user));
        Member nonCreator = MemberFixture.create("nonCreator", "nonCreator@naver.com", Role.ADMIN);
        AuthMember nonCreatorAdmin = MemberFixture.createUser(nonCreator);

        // when
        pinCommandService.deletePinComment(nonCreatorAdmin, pinComment.getId());

        // then
        assertThat(pinCommentRepository.existsById(pinComment.getId())).isFalse();
    }

    static Stream<Arguments> publicAndPrivateTopicsStatus() {
        return Stream.of(
                Arguments.of(PUBLIC, ALL_MEMBERS),
                Arguments.of(PUBLIC, GROUP_ONLY),
                Arguments.of(PRIVATE, GROUP_ONLY)
        );
    }

}
