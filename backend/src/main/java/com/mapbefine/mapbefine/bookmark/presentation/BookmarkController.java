package com.mapbefine.mapbefine.bookmark.presentation;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.application.BookmarkCommandService;
import com.mapbefine.mapbefine.bookmark.application.BookmarkQueryService;
import com.mapbefine.mapbefine.common.interceptor.LoginRequired;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkCommandService bookmarkCommandService;

    private final BookmarkQueryService bookmarkQueryService;

    public BookmarkController(
            BookmarkCommandService bookmarkCommandService,
            BookmarkQueryService bookmarkQueryService
    ) {
        this.bookmarkCommandService = bookmarkCommandService;
        this.bookmarkQueryService = bookmarkQueryService;
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<Void> addTopicInBookmark(
            AuthMember authMember,
            @RequestParam Long topicId
    ) {
        Long bookmarkId = bookmarkCommandService.addTopicInBookmark(authMember, topicId);

        return ResponseEntity.created(URI.create("/bookmarks/" + bookmarkId)).build();
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<TopicResponse>> findAllTopicsInBookmark(AuthMember authMember) {
        List<TopicResponse> responses = bookmarkQueryService.findAllTopicsInBookmark(authMember);

        return ResponseEntity.ok(responses);
    }

    @LoginRequired
    @DeleteMapping
    public ResponseEntity<Void> deleteTopicInBookmark(
            AuthMember authMember,
            @RequestParam Long topicId
    ) {
        bookmarkCommandService.deleteTopicInBookmark(authMember, topicId);

        return ResponseEntity.noContent().build();
    }

}
