package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.entity.Coordinate;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class PinQueryServiceTest {

    @Autowired
    private PinQueryService pinQueryService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    private Location location;

    private Coordinate coordinate;

    private Topic topic;

    @BeforeEach
    void setUp() {
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        coordinate = new Coordinate(latitude, longitude);
        location = saveLocation(coordinate);
        topic = topicRepository.save(new Topic("topicName", "topicDescription"));
    }

    @Test
    @DisplayName("핀 목록을 가져온다.")
    void findAll_Success() {
        // given
        List<PinResponse> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Pin pin = Pin.createPinAssociatedWithLocationAndTopic("name", "description", location, topic);
            Long savedId = pinRepository.save(pin).getId();
            expected.add(new PinResponse(
                    savedId,
                    "name",
                    "road",
                    "description",
                    coordinate.getLatitude().toString(),
                    coordinate.getLongitude().toString())
            );
        }

        // when
        List<PinResponse> responses = pinQueryService.findAll();

        // then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀의 Id 를 넘기면 핀을 가져온다.")
    void findById_Success() {
        // given

        // when

        // then
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

}
