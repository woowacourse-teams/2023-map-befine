package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.LocationFixture;
import com.mapbefine.mapbefine.PinFixture;
import com.mapbefine.mapbefine.TopicFixture;
import com.mapbefine.mapbefine.dto.TopicDetailResponse;
import com.mapbefine.mapbefine.dto.TopicFindBestRequest;
import com.mapbefine.mapbefine.dto.TopicResponse;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicQueryServiceTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicRepository topicRepository;

    private com.mapbefine.mapbefine.service.TopicQueryService topicQueryService;
    private Topic TOPIC_BEST_3RD;
    private Topic TOPIC_BEST_2ND;
    private Topic TOPIC_BEST_1ST;
    private Location ALL_PINS_LOCATION;

    @BeforeEach
    void setup() {
        topicQueryService = new com.mapbefine.mapbefine.service.TopicQueryService(topicRepository, locationRepository);

        ALL_PINS_LOCATION = LocationFixture.createByCoordinate(35.0, 127.0);
        locationRepository.save(ALL_PINS_LOCATION);

        TOPIC_BEST_3RD = createAndSaveByNameAndPinCounts("준팍의 또간집", 1);
        TOPIC_BEST_2ND = createAndSaveByNameAndPinCounts("도이의 또간집", 2);
        TOPIC_BEST_1ST = createAndSaveByNameAndPinCounts("패트릭의 또간집", 3);
    }

    private Topic createAndSaveByNameAndPinCounts(String topicName, int pinCounts) {
        Topic topic = TopicFixture.createByName(topicName);
        for (int i = 0; i < pinCounts; i++) {
            PinFixture.create(ALL_PINS_LOCATION, topic);
        }
        return topicRepository.save(topic);
    }

    @Test
    @DisplayName("모든 Topic 목록을 조회한다.")
    void findAll() {
        // given
        // when
        List<TopicResponse> topics = topicQueryService.findAll();

        // then
        assertThat(topics).contains(TopicResponse.from(TOPIC_BEST_3RD));
    }

    @Test
    @DisplayName("해당 ID를 가진 Topic을 조회한다.")
    void findById() {
        // given
        // when
        TopicDetailResponse actual = topicQueryService.findById(TOPIC_BEST_3RD.getId());

        // then
        assertThat(actual).isEqualTo(TopicDetailResponse.from(TOPIC_BEST_3RD));
    }

    @Test
    @DisplayName("주어진 좌표 3KM 이내의 Topic들을 Pin 개수 순서대로 조회한다.")
    void findBests() {
        // given

        TopicFindBestRequest currentLocation = new TopicFindBestRequest(
                ALL_PINS_LOCATION.getLatitude().toString(),
                ALL_PINS_LOCATION.getLongitude().toString()
        );

        // when
        List<TopicResponse> currentTopics = topicQueryService.findBests(currentLocation);

        // then
        assertThat(currentTopics.get(0)).isEqualTo(TopicResponse.from(TOPIC_BEST_1ST));
        assertThat(currentTopics.get(1)).isEqualTo(TopicResponse.from(TOPIC_BEST_2ND));
        assertThat(currentTopics.get(2)).isEqualTo(TopicResponse.from(TOPIC_BEST_3RD));
    }

}
