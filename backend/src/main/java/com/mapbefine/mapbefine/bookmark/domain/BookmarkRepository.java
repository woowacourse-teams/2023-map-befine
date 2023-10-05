package com.mapbefine.mapbefine.bookmark.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByMemberIdAndTopicId(Long memberId, Long topicId);

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    void deleteAllByMemberId(Long memberId);

    void deleteAllByTopicId(Long topicId);

}
