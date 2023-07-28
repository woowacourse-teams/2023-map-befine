package com.mapbefine.mapbefine.config;

import com.mapbefine.mapbefine.repository.MemberRepository;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    public WebMvcConfig(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .exposedHeaders("Location");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(memberRepository));
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new StringToPermissionConverter());
        registry.addConverter(new StringToPublicityConverter());
    }

}
