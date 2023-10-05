package com.mapbefine.mapbefine.bookmark.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    void deleteAllByMemberId(Long memberId);

    Optional<Bookmark> findByMemberIdAndTopicId(Long memberId, Long topicId);

}
