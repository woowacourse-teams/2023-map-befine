package com.mapbefine.mapbefine.topic.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = {"creator", "permissions"})
    Optional<Topic> findById(Long id);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAll();

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAllByOrderByLastPinUpdatedAtDesc();

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAllByCreatorId(Long creatorId);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findTopicsByBookmarksMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.id = :topicId")
    void deleteById(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.creator.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

}
