package com.mapbefine.mapbefine.bookmark.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.mapbefine.mapbefine.bookmark.application.BookmarkCommandService;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class BookmarkControllerTest extends RestDocsIntegration {

    @MockBean
    private BookmarkCommandService bookmarkCommandService;

    @Test
    @DisplayName("토픽을 회원의 즐겨찾기에 추가")
    void addTopicInBookmark() throws Exception {
        // TODO: 2023/12/03 void는 아마 ... 아무것도 안하는걸로 아는데 ?
//        given(bookmarkCommandService.addTopicInBookmark(any(), any())).willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookmarks/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("회원의 토픽 즐겨찾기 목록 삭제")
    void deleteTopicInBookmark() throws Exception {
        doNothing().when(bookmarkCommandService).deleteTopicInBookmark(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/bookmarks/topics/1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }

}
