package com.mapbefine.mapbefine;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;


public abstract class TestDatabaseContainer {

    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.32");

    static {
        mySQLContainer.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", TestDatabaseContainer::getJdbcUrlWithQueryStrings);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
    }

    private static String getJdbcUrlWithQueryStrings() {
        return mySQLContainer.getJdbcUrl() + "?rewriteBatchedStatements=true";
    }

}

