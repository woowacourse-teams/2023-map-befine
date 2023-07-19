package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.dto.TopicDetailResponse;
import com.mapbefine.mapbefine.dto.TopicFindBestRequest;
import com.mapbefine.mapbefine.dto.TopicResponse;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@TestPropertySource(properties = {
        "spring.jpa.defer-datasource-initialization=true"
})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/data-topic-query.sql")
class TopicQueryServiceTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicRepository topicRepository;

    private TopicQueryService topicQueryService;

    @BeforeEach
    void setup() {
        topicQueryService = new TopicQueryService(topicRepository, locationRepository);
    }

    @Test
    @DisplayName("모든 Topic 목록을 조회한다.")
    void findAll() {
        // given
        // when
        List<TopicResponse> topics = topicQueryService.findAll();

        // then
        assertThat(topics).isNotEmpty();
    }

    @Test
    @DisplayName("해당 ID를 가진 Topic을 조회한다.")
    void findById() {
        // given
        List<TopicResponse> topics = topicQueryService.findAll();
        TopicResponse expected = topics.get(0);

        // when
        TopicDetailResponse actual = topicQueryService.findById(expected.id());

        // then
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.name()).isEqualTo(expected.name());
    }

    @Test
    @DisplayName("주어진 좌표 3KM 이내의 Topic들을 Pin 개수 순서대로 조회한다.")
    void findBests() {
        // given
        // when
        List<TopicResponse> currentTopics = topicQueryService.findBests(new TopicFindBestRequest("0", "0"));
        System.out.println(currentTopics);

        // then
        TopicResponse first = currentTopics.get(0);
        TopicResponse second = currentTopics.get(1);
        TopicResponse third = currentTopics.get(2);

        assertThat(first.name()).isEqualTo("패트릭의 또간집");
        assertThat(second.name()).isEqualTo("도이의 또간집");
        assertThat(third.name()).isEqualTo("준팍의 또간집");
    }
}
