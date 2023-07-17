package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {
}
