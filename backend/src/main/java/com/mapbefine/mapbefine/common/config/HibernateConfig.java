package com.mapbefine.mapbefine.common.config;

import com.mapbefine.mapbefine.common.filter.QueryInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final QueryInspector queryInspector;

    public HibernateConfig(QueryInspector queryInspector) {
        this.queryInspector = queryInspector;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties ->
                hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, queryInspector);
    }

}
