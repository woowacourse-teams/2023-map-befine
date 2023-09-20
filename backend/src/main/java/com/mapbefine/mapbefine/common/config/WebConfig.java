package com.mapbefine.mapbefine.common.config;

import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.mapbefine.mapbefine.common.converter.OauthServerTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://mapbefine.kro.kr", "https://mapbefine.com")
                .allowedHeaders(COOKIE)
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders(LOCATION, SET_COOKIE);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
    }

}
