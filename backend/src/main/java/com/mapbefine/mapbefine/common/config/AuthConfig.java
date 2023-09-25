package com.mapbefine.mapbefine.common.config;

import com.mapbefine.mapbefine.common.interceptor.AdminAuthInterceptor;
import com.mapbefine.mapbefine.common.interceptor.AuthInterceptor;
import com.mapbefine.mapbefine.common.resolver.AuthArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor;
    private final AuthInterceptor authInterceptor;
    private final AuthArgumentResolver authArgumentResolver;

    public AuthConfig(
            AdminAuthInterceptor adminAuthInterceptor,
            AuthInterceptor authInterceptor,
            AuthArgumentResolver authArgumentResolver
    ) {
        this.adminAuthInterceptor = adminAuthInterceptor;
        this.authInterceptor = authInterceptor;
        this.authArgumentResolver = authArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/admin/**");
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}
