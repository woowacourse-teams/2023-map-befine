package com.mapbefine.mapbefine.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    List<Long> findAllIdTopicIdByIdMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.id.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.id.topicId = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

}
