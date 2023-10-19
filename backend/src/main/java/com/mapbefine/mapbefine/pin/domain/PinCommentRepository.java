package com.mapbefine.mapbefine.pin.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinCommentRepository extends JpaRepository<PinComment, Long> {

    List<PinComment> findAllByPinId(Long pinId);

}
