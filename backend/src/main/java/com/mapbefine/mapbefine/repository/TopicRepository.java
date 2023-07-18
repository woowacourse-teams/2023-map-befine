package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
