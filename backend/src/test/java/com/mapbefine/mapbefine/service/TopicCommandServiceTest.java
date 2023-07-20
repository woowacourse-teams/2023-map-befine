package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.LocationFixture;
import com.mapbefine.mapbefine.PinFixture;
import com.mapbefine.mapbefine.TopicFixture;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicCommandServiceTest {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LocationRepository locationRepository;

    private TopicCommandService topicCommandService;

    private Topic TOPIC_WITH_TWO_PINS;

    @BeforeEach
    void setup() {
        topicCommandService = new TopicCommandService(topicRepository, pinRepository);

        TOPIC_WITH_TWO_PINS = TopicFixture.createByName("준팍의 또간집");

        Location location = LocationFixture.create();
        locationRepository.save(location);

        PinFixture.create(location, TOPIC_WITH_TWO_PINS);
        PinFixture.create(location, TOPIC_WITH_TWO_PINS);

        topicRepository.save(TOPIC_WITH_TWO_PINS);
    }

    @Test
    @DisplayName("Topic의 이름이나 설명을 수정하면 DB에 반영한다.")
    void update() {
        //given
        Long id = TOPIC_WITH_TWO_PINS.getId();

        //when
        String name = "준팍의 안갈 집";
        String description = "다시는 안갈 집";
        topicCommandService.update(id, new TopicUpdateRequest(name, description));

        //then
        Topic topic = topicRepository.findById(id).get();
        assertThat(topic.getName()).isEqualTo(name);
        assertThat(topic.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("Topic을 삭제하면 토픽과 안의 Pin들이 isDeleted가 true로 변한다")
    void delete() {
        //given
        Long id = TOPIC_WITH_TWO_PINS.getId();

        //when
        topicCommandService.delete(id);

        //then
        Topic deletedTopic = topicRepository.findById(id).get();
        assertThat(deletedTopic.getId()).isEqualTo(id);
        assertThat(deletedTopic.isDeleted()).isTrue();
        for (Pin pin : deletedTopic.getPins()) {
            assertThat(pin.isDeleted()).isTrue();
        }
    }

}
