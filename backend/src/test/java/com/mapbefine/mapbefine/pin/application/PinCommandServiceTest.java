package com.mapbefine.mapbefine.pin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.history.application.PinHistoryCommandService;
import com.mapbefine.mapbefine.image.FileFixture;
import com.mapbefine.mapbefine.image.exception.ImageException.ImageBadRequestException;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

@ServiceTest
class PinCommandServiceTest {

    private static final MultipartFile BASE_IMAGE_FILE = FileFixture.createFile();
    private static final String BASE_IMAGE = "https://mapbefine.github.io/favicon.png";

    @MockBean
    private PinHistoryCommandService pinHistoryCommandService;
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

    private Location location;
    private Topic topic;
    private AuthMember authMember;
    private PinCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(MemberFixture.create("user1", "userfirst@naver.com", Role.ADMIN));
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
    @DisplayName("핀을 추가하면 핀 정보 이력을 저장한다.")
    void save_Success_SaveHistory() {
        // when
        pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        // then
        verify(pinHistoryCommandService, times(1)).saveHistory(any(PinUpdateEvent.class));
    }

    @Test
    @DisplayName("핀 추가 시 예외가 발생하면, 정보 이력도 저장하지 않는다.")
    void save_Fail_DoNotSaveHistory() {
        // when
        assertThatThrownBy(() -> pinCommandService.save(authMember, Collections.emptyList(), null))
                .isInstanceOf(RuntimeException.class);

        // then
        verify(pinHistoryCommandService, never()).saveHistory(any(PinUpdateEvent.class));
    }

    @Test
    @DisplayName("핀 정보 이력 저장 시 예외가 발생하면, 추가된 핀 정보도 저장하지 않는다.")
    void save_FailBySaveHistoryException() {
        // given
        doThrow(new RuntimeException()).when(pinHistoryCommandService).saveHistory(any(PinUpdateEvent.class));

        // when
        // then
        assertThatThrownBy(() -> pinCommandService.save(new Guest(), List.of(BASE_IMAGE_FILE), createRequest))
                .isInstanceOf(RuntimeException.class);
        assertThat(pinRepository.findAll()).isEmpty();
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
    @DisplayName("핀을 변경하면 핀 정보 이력을 저장한다.")
    void update_Success_SaveHistory() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        // when
        pinCommandService.update(authMember, pinId, new PinUpdateRequest("name", "update"));

        // then
        verify(pinHistoryCommandService, times(2)).saveHistory(any(PinUpdateEvent.class));
    }

    @Test
    @DisplayName("핀 수정 시 예외가 발생하면, 정보 이력도 저장하지 않는다.")
    void update_Fail_DoNotSaveHistory() {
        // given
        long illegalPinId = -1L;

        // when
        assertThatThrownBy(
                () -> pinCommandService.update(new Guest(), illegalPinId, new PinUpdateRequest("name", "update"))
        ).isInstanceOf(PinForbiddenException.class);

        // then
        verify(pinHistoryCommandService, never()).saveHistory(any(PinUpdateEvent.class));
    }

    @Test
    @DisplayName("핀 정보 이력 저장 시 예외가 발생하면, 수정된 핀 정보도 저장하지 않는다.")
    void update_FailBySaveHistoryException() {
        // given
        long pinId = pinCommandService.save(authMember, List.of(BASE_IMAGE_FILE), createRequest);

        // when
        doThrow(new RuntimeException()).when(pinHistoryCommandService).saveHistory(any(PinUpdateEvent.class));
        PinUpdateRequest request = new PinUpdateRequest("name", "update");

        // then
        assertThatThrownBy(() -> pinCommandService.update(authMember, pinId, request))
                .isInstanceOf(RuntimeException.class);
        pinRepository.findById(pinId)
                .ifPresentOrElse(
                        pin -> assertThat(pin.getUpdatedAt()).isEqualTo(pin.getCreatedAt()),
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

}
