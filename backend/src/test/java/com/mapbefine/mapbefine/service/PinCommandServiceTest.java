package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.entity.Coordinate;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
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

@Transactional
@SpringBootTest
class PinCommandServiceTest {
    @Autowired
    private PinCommandService pinCommandService;

    @Autowired
    private PinQueryService pinQueryService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Topic topic;

    @BeforeEach
    void setUp() {
        topic = topicRepository.save(new Topic("topicName", "topicDescription"));
    }

    @Test
    @DisplayName("핀을 저장하려는 위치(Location)가 존재하면 해당 위치에 핀을 저장한다.")
    public void saveIfExistLocation_Success() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        Coordinate coordinate = new Coordinate(latitude, longitude);
        Location location = new Location(
                "parcel",
                "road",
                coordinate,
                "legalDongCode"
        );
        locationRepository.save(location);

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
                null
        );

        assertThat(location.getPins()).extractingResultOf("getId")
                .contains(savedPinId);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀을 저장하려는 존재하지 않는 위치(Location)가 존재하지 않으면 위치를 저장하고 핀을 저장한다.")
    public void saveIfNotExistLocation_Success() {
        // given
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        locationRepository.deleteAll();
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
                null
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
}