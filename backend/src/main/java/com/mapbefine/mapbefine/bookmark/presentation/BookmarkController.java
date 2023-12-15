package com.mapbefine.mapbefine.bookmark.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.application.BookmarkCommandService;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkCommandService bookmarkCommandService;

    public BookmarkController(BookmarkCommandService bookmarkCommandService) {
        this.bookmarkCommandService = bookmarkCommandService;
    }

    @LoginRequired
    @PostMapping("/topics/{topicId}")
    public ResponseEntity<Void> addTopicInBookmark(AuthMember authMember, @PathVariable Long topicId) {
        bookmarkCommandService.addTopicInBookmark(authMember, topicId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @DeleteMapping("/topics/{topicId}")
    public ResponseEntity<Void> deleteTopicInBookmark(AuthMember authMember, @PathVariable Long topicId) {
        bookmarkCommandService.deleteTopicInBookmark(authMember, topicId);

        return ResponseEntity.noContent().build();
    }

}
