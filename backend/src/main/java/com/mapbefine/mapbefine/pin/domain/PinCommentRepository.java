package com.mapbefine.mapbefine.pin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinCommentRepository extends JpaRepository<PinComment, Long> {

}
