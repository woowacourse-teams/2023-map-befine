package com.mapbefine.mapbefine.permission.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByTopicId(Long topicId);

    boolean existsByTopicIdAndMemberId(Long topicId, Long memberId);

}
