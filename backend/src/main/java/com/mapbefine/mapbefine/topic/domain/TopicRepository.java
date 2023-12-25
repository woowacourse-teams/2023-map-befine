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

    @EntityGraph(attributePaths = {"creator"})
    Optional<Topic> findById(Long id);

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findAll();

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findAllByOrderByLastPinUpdatedAtDesc();

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findAllByCreatorId(Long creatorId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.bookmarkCount = t.bookmarkCount - 1 where t.id = :topicId")
    void decreaseBookmarkCountById(@Param("topicId") Long topicId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Topic t set t.bookmarkCount = t.bookmarkCount + 1 where t.id = :topicId")
    void increaseBookmarkCountById(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.id = :topicId")
    void deleteById(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.creator.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

}
