package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.util.Collections;
import java.util.List;
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

    private TopicCommandService topicCommandService;

    @BeforeEach
    void setup() {
        topicCommandService = new TopicCommandService(topicRepository, pinRepository);

    }

    @Test
    @DisplayName("Topic의 이름이나 설명을 수정하면 DB에 반영한다.")
    void update() {
        //given
        TopicCreateRequest topicCreateRequest = new TopicCreateRequest(
                "준팍의 또간집",
                "준팍이 두번 간 집",
                Collections.emptyList()
        );
        long topicId = topicCommandService.createNew(topicCreateRequest);

        //when
        String name = "준팍의 안갈 집";
        String description = "다시는 안갈 집";
        topicCommandService.update(topicId, new TopicUpdateRequest(name, description));

        Topic topic = topicRepository.findById(topicId).get();
        //then
        assertThat(topic.getName()).isEqualTo(name);
        assertThat(topic.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("Topic을 삭제하면 토픽과 안의 Pin들이 isDeleted가 true로 변한다")
    void delete() {
        //given
        List<Topic> all = topicRepository.findAll();
        Topic topic = all.get(0);

        //when
        Long id = topic.getId();
        topicCommandService.delete(id);

        Topic deletedTopic = topicRepository.findById(id).get();

        //then
        assertThat(deletedTopic.getId()).isEqualTo(id);
        assertThat(deletedTopic.isDeleted()).isTrue();
    }


}
