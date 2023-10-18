package com.mapbefine.mapbefine.atlas.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AtlasRepository extends JpaRepository<Atlas, Long> {

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    void deleteByMemberIdAndTopicId(Long memberId, Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Atlas a where a.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Atlas a where a.topic.id = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);
}
