package com.mapbefine.mapbefine.permission.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByTopic(Topic topic);

    boolean existsByTopicIdAndMemberId(Long topicId, Long memberId);

}
