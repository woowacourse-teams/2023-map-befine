package com.mapbefine.mapbefine.atlas.presentation;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.atlas.application.AtlasCommandService;
import com.mapbefine.mapbefine.common.RestDocsIntegration;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class AtlasControllerTest extends RestDocsIntegration {

    @MockBean
    private AtlasCommandService atlasCommandService;

    private final String authHeader = Base64.encodeBase64String("Basic member@naver.com".getBytes());

    @Test
    @DisplayName("모아보기에 지도를 추가한다")
    void addTopicToAtlas() throws Exception {
        // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/atlas/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

    @Test
    @DisplayName("모아보기에 추가되어있는 지도를 삭제한다")
    void removeTopicFromAtlas() throws Exception {
        // then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/atlas/1")
                        .header(AUTHORIZATION, authHeader)
        ).andDo(restDocs.document());
    }

}
