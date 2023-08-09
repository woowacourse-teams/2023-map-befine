package com.mapbefine.mapbefine.pin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
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
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PinCommandServiceTest {

    private static final String BASE_IMAGE = "https://map-befine-official.github.io/favicon.png";

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
    private Member member;
    private AuthMember authMember;
    private PinCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("user1", "userfirst@naver.com", Role.USER));
        location = locationRepository.save(LocationFixture.create());
        topic = topicRepository.save(TopicFixture.createByName("topic", member));

        authMember = AuthMember.from(member);
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
        long savedPinId = pinCommandService.save(authMember, createRequest);
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, savedPinId);
        Pin pin = pinRepository.findById(savedPinId).get();
        Location savedLocation = pin.getLocation();

        // then
        assertThat(savedLocation.getId()).isEqualTo(location.getId());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(PinDetailResponse.from(pin));
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
        long savedPinId = pinCommandService.save(authMember, createRequest);
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, savedPinId);
        Pin pin = pinRepository.findById(savedPinId).get();
        Location savedLocation = pin.getLocation();

        // then
        assertThat(savedLocation.getId()).isNotEqualTo(location.getId());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(PinDetailResponse.from(pin));

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(2);
        assertThat(locations)
                .extractingResultOf("getId")
                .containsExactly(location.getId(), savedLocation.getId());
    }

    @Test
    @DisplayName("권한이 없는 토픽에 핀을 저장하면 예외를 발생시킨다.")
    void save_FailByForbidden() {
        
        assertThatThrownBy(() -> pinCommandService.save(new Guest(), createRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("권한이 없는 토픽에 핀을 수정하면 예외를 발생시킨다.")
    void update_FailByForbidden() {
        long pinId = pinCommandService.save(authMember, createRequest);

        assertThatThrownBy(() -> pinCommandService.update(
                new Guest(), pinId, new PinUpdateRequest("name", "description"))
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("핀을 삭제하면 해당 핀을 soft delete 하고, 해당 핀의 이미지들도 soft delete 한다.")
    void removeById_Success() {
        // given
        long pinId = pinCommandService.save(authMember, createRequest);
        Pin pin = pinRepository.findById(pinId).get();
        PinImage.createPinImageAssociatedWithPin(BASE_IMAGE, pin);

        // when
        pinCommandService.removeById(authMember, pinId);

        // then
        pinRepository.findById(pinId)
                .ifPresentOrElse(
                        found -> assertThat(found.isDeleted()).isTrue(),
                        Assertions::fail
                );
        assertThat(pinImageRepository.findAllByPinId(pinId))
                .extractingResultOf("isDeleted")
                .containsOnly(true);
    }

    @Test
    @DisplayName("권한이 없는 토픽의 핀을 삭제하면 예외를 발생시킨다.")
    void removeById_FailByForbidden() {
        long pinId = pinCommandService.save(authMember, createRequest);

        assertThatThrownBy(() -> pinCommandService.removeById(new Guest(), pinId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("핀 id를 전달받아 해당하는 핀에 핀 이미지를 추가한다.")
    void addImage_Success() {
        // given
        long pinId = pinCommandService.save(authMember, createRequest);

        // when
        long pinImageId = savePinImageAndGetId(pinId);

        // then
        pinImageRepository.findById(pinImageId)
                .ifPresentOrElse(
                        found -> assertThat(found.getImageUrl()).isEqualTo(BASE_IMAGE),
                        Assertions::fail
                );;
    }

    @Test
    @DisplayName("권한이 없는 토픽의 핀에 핀 이미지를 추가하면 예외를 발생시킨다.")
    void addImage_FailByForbidden() {
        // given
        long pinId = pinCommandService.save(authMember, createRequest);

        // when, then
        assertThatThrownBy(() -> pinCommandService.addImage(new Guest(), new PinImageCreateRequest(pinId, BASE_IMAGE)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("핀 이미지의 id를 전달받아 해당하는 핀 이미지를 soft delete 한다.")
    void removeImageById_Success() {
        // given
        long pinId = pinCommandService.save(authMember, createRequest);
        long pinImageId = savePinImageAndGetId(pinId);

        // when
        pinCommandService.removeImageById(authMember, pinImageId);

        // then
        pinImageRepository.findById(pinImageId)
                        .ifPresentOrElse(
                                found -> assertThat(found.isDeleted()).isTrue(),
                                Assertions::fail
                        );
    }

    private long savePinImageAndGetId(long pinId) {
        pinCommandService.addImage(authMember, new PinImageCreateRequest(pinId, BASE_IMAGE));
        List<PinImageResponse> pinImages = pinQueryService.findDetailById(authMember, pinId)
                .images();
        PinImageResponse pinImage = pinImages.get(0);
        return pinImage.id();
    }

    @Test
    @DisplayName("권한이 없는 토픽의 핀 이미지를 삭제하면 예외를 발생시킨다.")
    void removeImageById_FailByForbidden() {
        long pinId = pinCommandService.save(authMember, createRequest);
        long pinImageId = savePinImageAndGetId(pinId);

        assertThatThrownBy(() -> pinCommandService.removeImageById(new Guest(), pinImageId))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
