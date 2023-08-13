package com.mapbefine.mapbefine.common.interceptor;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.auth.infrastructure.AuthorizationExtractor;
import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor<AuthInfo> authorizationExtractor;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(
            AuthorizationExtractor<AuthInfo> authorizationExtractor,
            AuthService authService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.authorizationExtractor = authorizationExtractor;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if (isAuthMemberNotRequired(handlerMethod)) {
            return true;
        }

        Long memberId = extractMemberIdFromToken(request);
        request.setAttribute("memberId", memberId);

        if (isLoginRequired((HandlerMethod) handler)) {
            // TODO: 2023/08/11 isMember false이면 403 반환
            return authService.isMember(memberId);
        }
        return true;
    }

    private boolean isAuthMemberNotRequired(HandlerMethod handlerMethod) {
        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
            if (parameter.getParameterType().equals(AuthMember.class)) {
                return false;
            }
        }
        return true;
    }

    private boolean isLoginRequired(HandlerMethod handlerMethod) {
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);

        return !Objects.isNull(loginRequired);
    }

    private Long extractMemberIdFromToken(HttpServletRequest request) {
        AuthInfo authInfo = authorizationExtractor.extract(request);
        if (Objects.isNull(authInfo)) {
            return null;
        }
        String accessToken = authInfo.accessToken();
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return Long.parseLong(jwtTokenProvider.getPayload(accessToken));
    }

}
