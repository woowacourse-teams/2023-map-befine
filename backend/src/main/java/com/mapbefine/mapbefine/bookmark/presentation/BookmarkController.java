package com.mapbefine.mapbefine.bookmark.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.application.BookmarkCommandService;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkCommandService bookmarkCommandService;

    public BookmarkController(BookmarkCommandService bookmarkCommandService) {
        this.bookmarkCommandService = bookmarkCommandService;
    }

    @LoginRequired
    @PostMapping("/topics")
    public ResponseEntity<Void> addTopicInBookmark(
            AuthMember authMember,
            @RequestParam Long id
    ) {
        Long bookmarkId = bookmarkCommandService.addTopicInBookmark(authMember, id);

        return ResponseEntity.created(URI.create("/bookmarks/topics" + bookmarkId)).build();
    }

    @LoginRequired
    @DeleteMapping("/topics")
    public ResponseEntity<Void> deleteTopicInBookmark(
            AuthMember authMember,
            @RequestParam Long id
    ) {
        bookmarkCommandService.deleteTopicInBookmark(authMember, id);

        return ResponseEntity.noContent().build();
    }

}
