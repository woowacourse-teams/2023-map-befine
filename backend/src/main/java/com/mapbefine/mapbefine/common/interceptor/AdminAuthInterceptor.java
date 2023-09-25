package com.mapbefine.mapbefine.common.interceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.auth.exception.AuthErrorCode;
import com.mapbefine.mapbefine.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final String secretKey;

    public AdminAuthInterceptor(
            @Value("${security.admin.key}") String secretKey
    ) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String secretKey = request.getHeader(AUTHORIZATION);
        validateAdmin(secretKey);
        return true;
    }

    private void validateAdmin(String key) {
        if (secretKey.equals(key)) {
            return;
        }
        throw new AuthException.AuthForbiddenException(AuthErrorCode.FORBIDDEN_ADMIN_ACCESS);
    }

}
