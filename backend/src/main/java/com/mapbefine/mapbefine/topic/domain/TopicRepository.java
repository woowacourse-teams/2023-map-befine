package com.mapbefine.mapbefine.topic.domain;

import com.mapbefine.mapbefine.topic.dto.TopicDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = {"creator"})
    Optional<Topic> findById(Long id);

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findAll();

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findAllByOrderByLastPinUpdatedAtDesc();

    @EntityGraph(attributePaths = {"creator"})
    List<Topic> findAllByCreatorId(Long creatorId);

    @Query(
            value = "SELECT new com.mapbefine.mapbefine.topic.dto.TopicDto(" +
                    "t.id, t.topicInfo.name, t.topicInfo.image.imageUrl, " +
                    "m.memberInfo.nickName, t.pinCount, t.bookmarkCount, t.lastPinUpdatedAt" +
                    ") " +
                    "FROM Topic t " +
                    "JOIN Bookmark b ON b.id.memberId = :memberId AND b.id.topicId = t.id " +
                    "JOIN Member m ON m.id = t.creator.id"
    )
    List<TopicDto> findTopicsInfoByBookmarksMemberId(@Param("memberId") Long memberId);

    @Query(
            value = "SELECT new com.mapbefine.mapbefine.topic.dto.TopicDto(" +
                    "t.id, t.topicInfo.name, t.topicInfo.image.imageUrl, " +
                    "m.memberInfo.nickName, t.pinCount, t.bookmarkCount, t.lastPinUpdatedAt" +
                    ") " +
                    "FROM Topic t " +
                    "JOIN Atlas a ON a.member.id = :memberId AND a.topic.id = t.id " +
                    "JOIN Member m ON m.id = t.creator.id"
    )
    List<TopicDto> findTopicsInfoByAtlasMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.id = :topicId")
    void deleteById(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("update Topic t set t.isDeleted = true where t.creator.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

}
