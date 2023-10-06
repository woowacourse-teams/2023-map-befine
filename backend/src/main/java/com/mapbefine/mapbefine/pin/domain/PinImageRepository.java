package com.mapbefine.mapbefine.pin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PinImageRepository extends JpaRepository<PinImage, Long> {

    Optional<PinImage> findByIdAndIsDeletedFalse(Long pinId);

    List<PinImage> findAllByPinIdAndIsDeletedFalse(Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update PinImage p set p.isDeleted = true where p.pin.id = :pinId")
    void deleteAllByPinId(@Param("pinId") Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update PinImage p set p.isDeleted = true where p.id = :id")
    void deleteById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update PinImage p set p.isDeleted = true where p.pin.id in :pinIds")
    void deleteAllByPinIds(@Param("pinIds") List<Long> pinIds);
}
