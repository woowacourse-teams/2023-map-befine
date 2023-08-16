package com.mapbefine.mapbefine.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    void deleteAllByMemberId(Long memberId);

    void deleteByMemberIdAndTopicId(Long memberId, Long topicId);
}
