package com.mapbefine.mapbefine.pin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PinCommandServiceTest {
    // TODO : Custom Annotation 인 @ServiceTest 를 붙여서 동작시키면, 중복된 Location 을 전혀 찾아오지 못하는 문제가 발생 (터지는 테스트는 @DisplayName("핀을 저장하려는 위치(Location)가 존재하면 해당 위치에 핀을 저장한다." 이 테스트))

    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

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


    private Topic topic;
    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
        member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        authMember = AuthMember.from(member);
        topic = topicRepository.save(
                Topic.createTopicAssociatedWithCreator(
                        "topicName",
                        "topicDescription",
                        "https://map-befine-official.github.io/favicon.png",
                        Publicity.PUBLIC,
                        Permission.ALL_MEMBERS,
                        member
                )
        );
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
                longitude,
                BASE_IMAGES
        );

        Long savedPinId = pinCommandService.save(authMember, request);

        // then
        PinDetailResponse actual = pinQueryService.findById(authMember, savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "name",
                "road",
                "description",
                latitude,
                longitude,
                null,
                BASE_IMAGES
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
                longitude,
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(authMember, request);

        // then
        PinDetailResponse actual = pinQueryService.findById(authMember, savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "name",
                "address",
                "description",
                latitude,
                longitude,
                null,
                BASE_IMAGES
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
    @DisplayName("제약 사항(name, description)을 지킨 정보로 핀의 정보를 수정하면 핀이 수정된다.")
    void update_Success() {
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
                longitude,
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(authMember, createRequest);

        // when
        PinUpdateRequest updateRequest = new PinUpdateRequest(
                "updatedName",
                "updatedDescription",
                BASE_IMAGES
        );
        pinCommandService.update(authMember, savedPinId, updateRequest);

        // then
        PinDetailResponse actual = pinQueryService.findById(authMember, savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "updatedName",
                "address",
                "updatedDescription",
                latitude,
                longitude,
                null,
                BASE_IMAGES
        );

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("제약 사항(name, description)을 지키지 않은 정보로 핀의 정보를 수정하면 핀이 수정되지 않는다.")
    void update_Fail() {
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
                longitude,
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(authMember, createRequest);

        // when then
        PinUpdateRequest updateRequest = new PinUpdateRequest("", "updatedDescription", BASE_IMAGES);
        assertThatThrownBy(() -> pinCommandService.update(authMember, savedPinId, updateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("핀을 삭제하면 soft-deleting 된다.")
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
                longitude,
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(authMember, createRequest);

        // when
        Pin pin = pinRepository.findById(savedPinId).get();
        assertThat(pin.isDeleted()).isFalse();

        pinCommandService.removeById(authMember, savedPinId);

        // then
        Pin deletedPin = pinRepository.findById(savedPinId).get();

        assertThat(deletedPin.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("토픽을 삭제하면 이와 관련된 모든 핀들이 soft-deleting 된다.")
    void removeAllByTopicId_Success() {
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
                longitude,
                BASE_IMAGES
        );

        for (int i = 0; i < 10; i++) {
            pinCommandService.save(authMember, createRequest);
        }

        // when
        Topic findTopicBeforeDeleting = topicRepository.findById(topic.getId()).get();
        assertThat(findTopicBeforeDeleting.getPins()).extractingResultOf("isDeleted")
                .doesNotContain(true);
//        pinCommandService.removeAllByTopicId(topic.getId()); // TODO : 이 메서드는 현재 테스트에서만 사용되고 있는 메서드 ??
        pinRepository.deleteAllByTopicId(topic.getId());

        // then
        Topic findTopicAfterDeleting = topicRepository.findById(topic.getId()).get();

        assertThat(findTopicAfterDeleting.getPins()).extractingResultOf("isDeleted")
                .doesNotContain(false);
    }
}
