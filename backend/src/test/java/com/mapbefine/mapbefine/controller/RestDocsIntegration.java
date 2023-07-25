package com.mapbefine.mapbefine.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class RestDocsIntegration {

    @Autowired
    protected ObjectMapper objectMapper;

    protected RestDocumentationResultHandler restDocs;

    protected MockMvc mockMvc;


    @BeforeEach
    void beforeEach(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.restDocs = MockMvcRestDocumentation.document("{class-name}/{method-name}");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(
                        MockMvcRestDocumentation.documentationConfiguration(provider)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint())
                )
                .alwaysDo(restDocs)
                .build();
    }

}
