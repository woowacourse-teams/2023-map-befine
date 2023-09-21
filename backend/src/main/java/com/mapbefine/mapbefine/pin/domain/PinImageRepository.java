package com.mapbefine.mapbefine.pin.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PinImageRepository extends JpaRepository<PinImage, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update PinImage p set p.isDeleted = true where p.pin.id = :pinId")
    void deleteAllByPinId(@Param("pinId") Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update PinImage p set p.isDeleted = true where p.id = :id")
    void deleteById(@Param("id") Long id);

    List<PinImage> findAllByPinId(Long pinId);
    
}
