package com.mapbefine.mapbefine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mapbefine.mapbefine.entity.Pin;

public interface PinRepository extends JpaRepository<Pin, Long> {
  
    @Modifying
    @Query("update Pin p set p.isDeleted = true where p.topic.id = :topidId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

    @Modifying
    @Query("update Pin p set p.isDeleted = true where p.id = :pinId")
    void deleteById(@Param("pinId") Long pinId);

    List<Pin> findAllByIsDeletedTrue();
  
}
