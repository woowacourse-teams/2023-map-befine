package com.mapbefine.mapbefine.member.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTopicBookmarkRepository extends JpaRepository<MemberTopicBookmark, Long> {

    public List<MemberTopicBookmark> findAllByMemberId(Long memberId);
}
