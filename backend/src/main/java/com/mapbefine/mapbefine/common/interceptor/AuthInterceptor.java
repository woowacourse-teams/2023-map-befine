package com.mapbefine.mapbefine.common.interceptor;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.auth.infrastructure.AuthorizationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor<AuthInfo> authorizationExtractor;
    private final AuthService authService;

    public AuthInterceptor(AuthorizationExtractor<AuthInfo> authorizationExtractor, AuthService authService) {
        this.authorizationExtractor = authorizationExtractor;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        if ((handler instanceof HandlerMethod) && isLoginRequired((HandlerMethod) handler)) {
            return isMember(request);
        }
        return true;
    }

    private boolean isLoginRequired(HandlerMethod handlerMethod) {
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);

        return !Objects.isNull(loginRequired);
    }

    private boolean isMember(HttpServletRequest request) {
        AuthInfo authInfo = authorizationExtractor.extract(request);

        return authService.isMember(authInfo);
    }
    
}
