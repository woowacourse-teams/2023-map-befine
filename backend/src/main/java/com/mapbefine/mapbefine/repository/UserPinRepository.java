package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.UserPin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPinRepository extends JpaRepository<UserPin, Long> {
}
