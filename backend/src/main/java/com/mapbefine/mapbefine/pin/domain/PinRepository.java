package com.mapbefine.mapbefine.pin.domain;

import com.mapbefine.mapbefine.pin.infrastructure.PinBatchRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long>, PinBatchRepositoryCustom {

    @EntityGraph(attributePaths = {"location", "topic", "creator", "pinImages"})
    List<Pin> findAll();

    @EntityGraph(attributePaths = {"location", "topic", "creator", "pinImages"})
    List<Pin> findAllByIdIn(List<Long> pinIds);

    @EntityGraph(attributePaths = {"location", "topic", "creator", "pinImages"})
    List<Pin> findAllByTopicId(Long topicId);

    @EntityGraph(attributePaths = {"location", "topic", "creator", "pinImages"})
    List<Pin> findAllByCreatorId(Long creatorId);

    @EntityGraph(attributePaths = {"location", "topic", "creator"})
    List<Pin> findAllByTopicIdIn(List<Long> topicIds);

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.topic.id = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.id = :pinId")
    void deleteById(@Param("pinId") Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.creator.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

}
