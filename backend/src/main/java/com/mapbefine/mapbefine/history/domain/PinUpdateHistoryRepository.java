package com.mapbefine.mapbefine.history.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PinUpdateHistoryRepository extends JpaRepository<PinUpdateHistory, Long> {

    List<PinUpdateHistory> findAllByPinId(Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update PinUpdateHistory p set p.isDeleted = true where p.pin.id = :pinId")
    void deleteAllByPinId(@Param("pinId") Long pinId);

    @Modifying(clearAutomatically = true)
    @Query("update PinUpdateHistory p set p.isDeleted = true where p.pin.id in :pinIds")
    void deleteAllByPinIds(@Param("pinIds") List<Long> pinIds);
}
