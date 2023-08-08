package com.mapbefine.mapbefine.common;

import com.mapbefine.mapbefine.DatabaseCleanup;
import io.restassured.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void tearDown() {
        databaseCleanup.execute();
    }

}
