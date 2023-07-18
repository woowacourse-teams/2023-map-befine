package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
