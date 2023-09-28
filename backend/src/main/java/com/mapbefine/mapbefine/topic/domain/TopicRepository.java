package com.mapbefine.mapbefine.topic.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = {"creator", "permissions"})
    Optional<Topic> findByIdAndIsDeletedFalse(Long id);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAllByIsDeletedFalse();

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findByIdInAndIsDeletedFalse(List<Long> ids);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAllByIsDeletedFalseOrderByLastPinUpdatedAtDesc();

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAllByCreatorIdAndIsDeletedFalse(Long creatorId);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findAllByCreatorId(Long creatorId);

    @EntityGraph(attributePaths = {"creator", "permissions"})
    List<Topic> findTopicsByBookmarksMemberIdAndIsDeletedFalse(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.id = :topicId")
    void deleteById(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.creator.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

}
