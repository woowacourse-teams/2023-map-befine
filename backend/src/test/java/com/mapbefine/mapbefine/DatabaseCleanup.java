package com.mapbefine.mapbefine;

import com.google.common.base.CaseFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DatabaseCleanup implements InitializingBean {

    private static final String SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE = "SET REFERENTIAL_INTEGRITY %s";
    private static final String TRUNCATE_SQL_MESSAGE = "TRUNCATE TABLE %s";
    private static final String ID_RESET_SQL_MESSAGE = "ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1";
    private static final String UNDERSCORE = "_";

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                .map(entityType -> convertTableNameFromCamelCaseToSnakeCase(entityType.getName()))
                .toList();
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery(String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, false)).executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery(String.format(TRUNCATE_SQL_MESSAGE, tableName)).executeUpdate();
            entityManager.createNativeQuery(String.format(ID_RESET_SQL_MESSAGE, tableName)).executeUpdate() ;
        }

        entityManager.createNativeQuery(String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, true)).executeUpdate();
    }

    private String convertTableNameFromCamelCaseToSnakeCase(String tableName) {
        StringBuilder tableNameSnake = new StringBuilder();

        for (char letter : tableName.toCharArray()) {
            addUnderScoreForCapitalLetter(tableNameSnake, letter);
            tableNameSnake.append(letter);
        }

        return tableNameSnake.substring(1).toLowerCase();
    }

    private void addUnderScoreForCapitalLetter(StringBuilder tableNameSnake, char letter) {
        if (Character.isUpperCase(letter)) {
            tableNameSnake.append(UNDERSCORE);
        }
    }

}
