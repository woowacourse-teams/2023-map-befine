package com.mapbefine.mapbefine.history.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinUpdateHistoryRepository extends JpaRepository<PinUpdateHistory, Long> {
    List<PinUpdateHistory> findAllByPinId(Long pinId);
}
