package com.mapbefine.mapbefine.bookmark.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.mapbefine.mapbefine.bookmark.application.BookmarkCommandService;
import com.mapbefine.mapbefine.bookmark.application.BookmarkQueryService;
import com.mapbefine.mapbefine.bookmark.dto.response.BookmarkResponse;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class BookmarkControllerTest extends RestDocsIntegration {

    @MockBean
    private BookmarkCommandService bookmarkCommandService;

    @MockBean
    private BookmarkQueryService bookmarkQueryService;

    @Test
    @DisplayName("토픽을 유저의 즐겨찾기에 추가")
    public void addTopicInBookmark() throws Exception {
        String authHeader = Base64.encodeBase64String("Basic member@naver.com".getBytes());

        given(bookmarkCommandService.addTopicInBookmark(any(), any())).willReturn(1L);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/bookmarks")
                        .header(AUTHORIZATION, authHeader)
                        .param("topicId", String.valueOf(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("유저의 토픽 즐겨찾기 목록 조회")
    public void findTopicsInBookmark() throws Exception {
        String authHeader = Base64.encodeBase64String("Basic member@naver.com".getBytes());

        List<BookmarkResponse> response = List.of(
                new BookmarkResponse(
                        1L,
                        1L,
                        "준팍의 또 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        3,
                        LocalDateTime.now()
                ),
                new BookmarkResponse(
                        2L,
                        2L,
                        "준팍의 두번째 토픽",
                        "https://map-befine-official.github.io/favicon.png",
                        5,
                        LocalDateTime.now()
                )
        );

        given(bookmarkQueryService.findAllTopicsInBookmark(any())).willReturn(response);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookmarks")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("유저의 토픽 즐겨찾기 목록 삭제")
    public void deleteTopicInBookmark() throws Exception {
        String authHeader = Base64.encodeBase64String("Basic member@naver.com".getBytes());

        doNothing().when(bookmarkCommandService).deleteTopicInBookmark(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/bookmarks")
                        .param("topicId", String.valueOf(1L))
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}