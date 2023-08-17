package com.mapbefine.mapbefine.atlas.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.mapbefine.mapbefine.atlas.application.AtlasCommandService;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class AtlasControllerTest extends RestDocsIntegration {

    @MockBean
    private AtlasCommandService atlasCommandService;

    @Test
    @DisplayName("모아보기에 지도를 추가한다")
    void addTopicToAtlas() throws Exception {
        //given
        doNothing().when(atlasCommandService).addTopic(any(), any());

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/atlas/topics?id=1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("모아보기에 추가되어있는 지도를 삭제한다")
    void removeTopicFromAtlas() throws Exception {
        doNothing().when(atlasCommandService).removeTopic(any(), any());

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/atlas/topics?id=1")
                        .header(AUTHORIZATION, testAuthHeaderProvider.createAuthHeaderById(1L))
        ).andDo(restDocs.document());
    }

}
