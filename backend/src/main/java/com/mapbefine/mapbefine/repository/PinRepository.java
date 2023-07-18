package com.mapbefine.mapbefine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mapbefine.mapbefine.entity.Pin;

public interface PinRepository extends JpaRepository<Pin, Long> {
}
