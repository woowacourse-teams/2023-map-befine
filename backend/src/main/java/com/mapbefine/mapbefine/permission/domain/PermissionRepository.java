package com.mapbefine.mapbefine.permission.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByTopicId(Long topicId);

    boolean existsByTopicIdAndMemberId(Long topicId, Long memberId);

    @Query(value = "SELECT p.topicId FROM Permission p WHERE p.memberId = :memberId")
    List<Long> findAllTopicIdsByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Permission p where p.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Permission p where p.topicId = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);
}
