package com.mapbefine.mapbefine.common.interceptor;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.auth.infrastructure.AuthorizationExtractor;
import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String UNAUTHORIZED_ERROR_MESSAGE = "로그인에 실패하였습니다.";
    private static final ErrorCode ILLEGAL_MEMBER_ID = new ErrorCode("03100", UNAUTHORIZED_ERROR_MESSAGE);
    private static final ErrorCode ILLEGAL_TOKEN = new ErrorCode("03101", UNAUTHORIZED_ERROR_MESSAGE);

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

        if (isLoginRequired((HandlerMethod) handler)) {
            validateMember(memberId);
        }

        request.setAttribute("memberId", memberId);

        return true;
    }

    private void validateMember(Long memberId) {
        if (authService.isMember(memberId)) {
            return;
        }

        throw new UnauthorizedException(ILLEGAL_MEMBER_ID);
    }

    private boolean isAuthMemberNotRequired(HandlerMethod handlerMethod) {
        return Arrays.stream(handlerMethod.getMethodParameters())
                .noneMatch(parameter -> parameter.getParameterType().equals(AuthMember.class));
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
            throw new UnauthorizedException(ILLEGAL_TOKEN);
        }
        return Long.parseLong(jwtTokenProvider.getPayload(accessToken));
    }

}
