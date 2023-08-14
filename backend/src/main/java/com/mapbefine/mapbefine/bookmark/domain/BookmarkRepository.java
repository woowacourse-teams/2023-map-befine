package com.mapbefine.mapbefine.bookmark.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);

    boolean existsByMemberIdAndTopicId(Long memberId, Long topicId);

    void deleteByMemberIdAndTopicId(Long memberId, Long topicId);
}
