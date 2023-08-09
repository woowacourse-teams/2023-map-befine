package com.mapbefine.mapbefine.atlas.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtlasRepository extends JpaRepository<Atlas, Long> {

    List<Atlas> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    void deleteByMemberIdAndTopicId(Long memberId, Long topicId);
}
