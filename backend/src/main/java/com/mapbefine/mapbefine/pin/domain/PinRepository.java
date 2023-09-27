package com.mapbefine.mapbefine.pin.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.topic.id = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.id = :pinId")
    void deleteById(@Param("pinId") Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.creator.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    List<Pin> findAll();

    @EntityGraph(attributePaths = {"location", "topic", "creator", "pinImages"})
    List<Pin> findAllByTopicId(Long topicId);

    @EntityGraph(attributePaths = {"location", "topic", "creator", "pinImages"})
    List<Pin> findAllByCreatorId(Long creatorId);


}
