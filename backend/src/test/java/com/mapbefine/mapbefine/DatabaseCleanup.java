package com.mapbefine.mapbefine;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleanup implements InitializingBean {

    private static final String TRUNCATE_SQL_MESSAGE = "TRUNCATE TABLE %s";
    private static final String ID_RESET_SQL_MESSAGE = "ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1";
    private static final String SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE = "SET REFERENTIAL_INTEGRITY %s";
    private static final String UNDERSCORE = "_";
    private static final String DISABLE_REFERENTIAL_QUERY = String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, false);
    private static final String ENABLE_REFERENTIAL_QUERY = String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, true);

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        Set<EntityType<?>> entities = entityManager.getMetamodel()
                .getEntities();

        tableNames = entities.stream()
                .filter(this::isEntity)
                .map(this::convertTableNameFromCamelCaseToSnakeCase)
                .toList();
    }

    private boolean isEntity(final EntityType<?> entityType) {
        return entityType.getJavaType()
                .getAnnotation(Entity.class) != null;
    }

    private String convertTableNameFromCamelCaseToSnakeCase(EntityType<?> entityType) {
        StringBuilder tableNameSnake = new StringBuilder();
        String classNameOfEntity = entityType.getName();

        for (char letter : classNameOfEntity.toCharArray()) {
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

    @Transactional
    public void execute() {
        executeSqlWithReferentialIntegrityDisabled(this::executeTruncate);
    }

    private void executeSqlWithReferentialIntegrityDisabled(Runnable sqlExecutor) {
        disableReferentialIntegrity();
        sqlExecutor.run();
        enableReferentialIntegrity();
    }

    private void disableReferentialIntegrity() {
        entityManager.flush();

        entityManager.createNativeQuery(DISABLE_REFERENTIAL_QUERY)
                .executeUpdate();
    }

    private void enableReferentialIntegrity() {

        entityManager.createNativeQuery(ENABLE_REFERENTIAL_QUERY)
                .executeUpdate();
    }

    private void executeTruncate() {
        for (String tableName : tableNames) {
            String TRUNCATE_QUERY = String.format(TRUNCATE_SQL_MESSAGE, tableName);
            String ID_RESET_QUERY = String.format(ID_RESET_SQL_MESSAGE, tableName);

            entityManager.createNativeQuery(TRUNCATE_QUERY)
                    .executeUpdate();
            entityManager.createNativeQuery(ID_RESET_QUERY)
                    .executeUpdate();
        }
    }

}
