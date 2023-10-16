package com.mapbefine.mapbefine.bookmark.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByMemberIdAndTopicId(Long memberId, Long topicId);

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.member.id = :memberId")
    void deleteAllByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.topic.id = :topicId")
    void deleteAllByTopicId(Long topicId);

}
