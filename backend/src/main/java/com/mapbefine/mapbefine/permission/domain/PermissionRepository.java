package com.mapbefine.mapbefine.permission.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByTopicId(Long topicId);

    boolean existsByTopicIdAndMemberId(Long topicId, Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Permission p where p.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Permission p where p.topic.id = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

}
