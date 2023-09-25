package com.mapbefine.mapbefine.common.annotation;

import com.mapbefine.mapbefine.common.config.JpaConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JpaConfig.class)
@DataJpaTest(
        includeFilters = {
                @Filter(type = FilterType.ANNOTATION, value = Service.class),
        },
        excludeFilters = {
                @Filter(
                        type = FilterType.REGEX,
                        pattern = "com.mapbefine.mapbefine.oauth.application.*"
                ),
                @Filter(
                        type = FilterType.REGEX,
                        pattern = "com.mapbefine.mapbefine.auth.application.*"
                )
        }
)
public @interface ServiceTest {
}
