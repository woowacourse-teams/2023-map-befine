package com.mapbefine.mapbefine.topic.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByIdIn(List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.id = :topicId")
    void deleteById(@Param("topicId") Long topicId);

    boolean existsById(Long id);

    List<Topic> findByCreatorId(Long creatorId);

    @Query("select new com.mapbefine.mapbefine.topic.domain.TopicWithBookmarkStatus("
            + "t, case when b.id is not null then true else false end"
            + ") "
            + "from Topic t "
            + "left join t.bookmarks b on b.member.id = :memberId")
    List<TopicWithBookmarkStatus> findAllWithBookmarkStatusByMemberId(
            @Param("memberId") Long memberId);


    @Query("select new com.mapbefine.mapbefine.topic.domain.TopicWithBookmarkStatus("
            + "t, case when b.id is not null then true else false end"
            + ") "
            + "from Topic t "
            + "left join t.bookmarks b on t.id = :topicId AND b.member.id = :memberId "
            + "where t.id = :topicId")
    Optional<TopicWithBookmarkStatus> findWithBookmarkStatusByIdAndMemberId(
            @Param("topicId") Long topicId,
            @Param("memberId") Long memberId
    );

    @Query("select new com.mapbefine.mapbefine.topic.domain.TopicWithBookmarkStatus("
            + "t, case when b.id is not null then true else false end"
            + " )"
            + "from Topic t "
            + "left join t.bookmarks b on b.member.id = :memberId "
            + "where t.id in :topicIds"
    )
    List<TopicWithBookmarkStatus> findWithBookmarkStatusByIdsAndMemberId(
            @Param("topicIds") List<Long> topicIds,
            @Param("memberId") Long memberId
    );

}
