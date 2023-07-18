package com.mapbefine.mapbefine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mapbefine.mapbefine.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
