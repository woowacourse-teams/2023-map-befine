package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;

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

}
