package com.mapbefine.mapbefine.pin.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
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
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinImageResponse;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.Collections;
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

    private Topic topic;
    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        topic = topicRepository.save(
                Topic.createTopicAssociatedWithMember(
                        "topicName",
                        "topicDescription",
                        "https://map-befine-official.github.io/favicon.png",
                        Publicity.PUBLIC,
                        Permission.ALL_MEMBERS,
                        member
                )
        );
        authMember = AuthMember.from(member);
    }

    @Test
    @DisplayName("핀을 저장하려는 위치(Location)가 존재하면 해당 위치에 핀을 저장한다.")
    void saveIfExistLocation_Success() {
        // given
        double latitude = 37.123456;
        double longitude = 127.123456;
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        Location location = saveLocation(coordinate);

        // when
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "road",
                "legalDongCode",
                latitude,
                longitude
        );

        Long savedPinId = pinCommandService.save(authMember, request);

        // then
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "name",
                "road",
                "description",
                latitude,
                longitude,
                null,
                Collections.emptyList()
        );

        assertThat(location.getPins()).extractingResultOf("getId")
                .contains(savedPinId);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    private Location saveLocation(Coordinate coordinate) {
        Address address = new Address(
                "parcel",
                "road",
                "legalDongCode"
        );

        Location location = new Location(
                address,
                coordinate
        );

        return locationRepository.save(location);
    }

    @Test
    @DisplayName("핀을 저장하려는 존재하지 않는 위치(Location)가 존재하지 않으면 위치를 저장하고 핀을 저장한다.")
    void saveIfNotExistLocation_Success() {
        // given
        double latitude = 37.123456;
        double longitude = 127.123456;

        // when
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude,
                longitude
        );
        Long savedPinId = pinCommandService.save(authMember, request);

        // then
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "name",
                "address",
                "description",
                latitude,
                longitude,
                null,
                Collections.emptyList()
        );

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(1);
        assertThat(locations.get(0).getPins()).hasSize(1);
        assertThat(locations.get(0).getPins())
                .extractingResultOf("getId")
                .containsExactly(savedPinId);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀을 삭제하면 해당 핀을 soft delete 하고, 해당 핀의 이미지들도 soft delete 한다.")
    void removeById_Success() {
        // given
        double latitude = 37.123456;
        double longitude = 127.123456;
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        saveLocation(coordinate);

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude,
                longitude
        );
        long pinId = pinCommandService.save(authMember, createRequest);
        Pin pin = pinRepository.findById(pinId).get();
        PinImage.createPinImageAssociatedWithPin(Image.of(BASE_IMAGE), pin);

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
    @DisplayName("핀 이미지의 id를 전달받아 해당하는 핀 이미지를 soft delete 한다.")
    void removeImage_Success() {
        // given
        double latitude = 37.123456;
        double longitude = 127.123456;
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        saveLocation(coordinate);

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude,
                longitude
        );
        long pinId = pinCommandService.save(authMember, createRequest);
        pinCommandService.addImage(authMember, new PinImageCreateRequest(pinId, BASE_IMAGE));
        List<PinImageResponse> pinImages = pinQueryService.findDetailById(authMember, pinId)
                .images();
        PinImageResponse pinImage = pinImages.get(0);
        Long pinImageId = pinImage.id();

        // when
        pinCommandService.removeImage(authMember, pinImageId);

        // then
        pinImageRepository.findById(pinImageId)
                        .ifPresentOrElse(
                                found -> assertThat(found.isDeleted()).isTrue(),
                                Assertions::fail
                        );
    }
}
