package com.mapbefine.mapbefine.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    @Modifying(clearAutomatically = true)
    void deleteAllByMemberId(Long memberId);

    Optional<Bookmark> findByMemberIdAndTopicId(Long memberId, Long topicId);

}
