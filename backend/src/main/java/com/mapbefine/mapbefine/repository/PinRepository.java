package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.topic.id = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Pin p set p.isDeleted = true where p.id = :pinId")
    void deleteById(@Param("pinId") Long pinId);

    List<Pin> findAllByIsDeletedTrue();

    List<Pin> findAllByTopicId(Long topicId);

}
