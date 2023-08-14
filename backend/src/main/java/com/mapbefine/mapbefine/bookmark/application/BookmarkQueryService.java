package com.mapbefine.mapbefine.bookmark.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.dto.response.BookmarkResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookmarkQueryService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkQueryService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<BookmarkResponse> findAllTopicsInBookmark(AuthMember authMember) {
        validateNonExistsMember(authMember);
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(authMember.getMemberId());

        return bookmarks.stream()
                .map(BookmarkResponse::from)
                .toList();
    }

    public void validateNonExistsMember(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
