package com.mapbefine.mapbefine.common.interceptor;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.auth.exception.AuthErrorCode;
import com.mapbefine.mapbefine.auth.exception.AuthException;
import com.mapbefine.mapbefine.auth.infrastructure.AuthorizationExtractor;
import com.mapbefine.mapbefine.auth.infrastructure.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor<AuthInfo> authorizationExtractor;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    public AdminAuthInterceptor(
            AuthorizationExtractor<AuthInfo> authorizationExtractor,
            AuthService authService,
            TokenProvider tokenProvider
    ) {
        this.authorizationExtractor = authorizationExtractor;
        this.authService = authService;
        this.tokenProvider = tokenProvider;
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

        Long memberId = extractMemberIdFromToken(request);

        validateAdmin(memberId);
        request.setAttribute("memberId", memberId);

        return true;
    }

    private Long extractMemberIdFromToken(HttpServletRequest request) {
        AuthInfo authInfo = authorizationExtractor.extract(request);
        if (Objects.isNull(authInfo)) {
            return null;
        }
        tokenProvider.validateAccessToken(authInfo.accessToken());

        return Long.parseLong(tokenProvider.getPayload(authInfo.accessToken()));
    }

    private void validateAdmin(Long memberId) {
        if (authService.isAdmin(memberId)) {
            return;
        }

        throw new AuthException.AuthForbiddenException(AuthErrorCode.FORBIDDEN_ADMIN_ACCESS);
    }

}
