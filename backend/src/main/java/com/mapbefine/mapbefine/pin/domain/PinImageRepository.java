package com.mapbefine.mapbefine.pin.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinImageRepository extends JpaRepository<PinImage, Long> {
    void deleteAllByPinId(Long pinId);

    List<PinImage> findAllByPinId(Long pinId);
    
}
