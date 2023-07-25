package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.entity.Coordinate;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class PinCommandServiceTest {
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

    private Topic topic;

    @BeforeEach
    void setUp() {
        topic = topicRepository.save(new Topic("topicName", "topicDescription", "https://map-befine-official.github.io/favicon.png"));
    }

    @Test
    @DisplayName("핀을 저장하려는 위치(Location)가 존재하면 해당 위치에 핀을 저장한다.")
    void saveIfExistLocation_Success() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        Coordinate coordinate = new Coordinate(latitude, longitude);
        Location location = saveLocation(coordinate);

        // when
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "road",
                "legalDongCode",
                latitude.toString(),
                longitude.toString(),
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(request);

        // then
        PinDetailResponse actual = pinQueryService.findById(savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "name",
                "road",
                "description",
                latitude.toString(),
                longitude.toString(),
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
        Location location = new Location(
                "parcel",
                "road",
                coordinate,
                "legalDongCode"
        );

        return locationRepository.save(location);
    }

    @Test
    @DisplayName("핀을 저장하려는 존재하지 않는 위치(Location)가 존재하지 않으면 위치를 저장하고 핀을 저장한다.")
    void saveIfNotExistLocation_Success() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);

        // when
        PinCreateRequest request = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude.toString(),
                longitude.toString(),
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(request);

        // then
        PinDetailResponse actual = pinQueryService.findById(savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "name",
                "address",
                "description",
                latitude.toString(),
                longitude.toString(),
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
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        Coordinate coordinate = new Coordinate(latitude, longitude);
        saveLocation(coordinate);

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude.toString(),
                longitude.toString(),
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(createRequest);

        // when
        PinUpdateRequest updateRequest = new PinUpdateRequest(
                "updatedName",
                "updatedDescription",
                BASE_IMAGES
        );
        pinCommandService.update(savedPinId, updateRequest);

        // then
        PinDetailResponse actual = pinQueryService.findById(savedPinId);
        PinDetailResponse expected = new PinDetailResponse(
                savedPinId,
                "updatedName",
                "address",
                "updatedDescription",
                latitude.toString(),
                longitude.toString(),
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
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        Coordinate coordinate = new Coordinate(latitude, longitude);
        saveLocation(coordinate);

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude.toString(),
                longitude.toString(),
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(createRequest);

        // when then
        PinUpdateRequest updateRequest = new PinUpdateRequest("", "updatedDescription",BASE_IMAGES);
        assertThatThrownBy(() -> pinCommandService.update(savedPinId, updateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("핀을 삭제하면 soft-deleting 된다.")
    void removeById_Success() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        Coordinate coordinate = new Coordinate(latitude, longitude);
        saveLocation(coordinate);

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude.toString(),
                longitude.toString(),
                BASE_IMAGES
        );
        Long savedPinId = pinCommandService.save(createRequest);

        // when
        Pin pin = pinRepository.findById(savedPinId).get();
        assertThat(pin.isDeleted()).isFalse();

        pinCommandService.removeById(savedPinId);

        // then
        Pin deletedPin = pinRepository.findById(savedPinId).get();

        assertThat(deletedPin.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("토픽을 삭제하면 이와 관련된 모든 핀들이 soft-deleting 된다.")
    void removeAllByTopicId_Success() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        Coordinate coordinate = new Coordinate(latitude, longitude);
        saveLocation(coordinate);

        PinCreateRequest createRequest = new PinCreateRequest(
                topic.getId(),
                "name",
                "description",
                "address",
                "legalDongCode",
                latitude.toString(),
                longitude.toString(),
                BASE_IMAGES
        );

        for (int i = 0; i < 10; i++) {
            pinCommandService.save(createRequest);
        }

        // when
        Topic findTopicBeforeDeleting = topicRepository.findById(topic.getId()).get();
        assertThat(findTopicBeforeDeleting.getPins()).extractingResultOf("isDeleted")
                .doesNotContain(true);
        pinCommandService.removeAllByTopicId(topic.getId());

        // then
        Topic findTopicAfterDeleting = topicRepository.findById(topic.getId()).get();

        assertThat(findTopicAfterDeleting.getPins()).extractingResultOf("isDeleted")
                .doesNotContain(false);
    }
}