package com.mapbefine.mapbefine.bookmark.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    public List<Bookmark> findAllByMemberId(Long memberId);

    public void deleteAllByMemberId(Long memberId);

    public boolean existsByIdAndMemberId(Long id, Long memberId);

}
