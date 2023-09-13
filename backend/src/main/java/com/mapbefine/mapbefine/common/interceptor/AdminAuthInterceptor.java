package com.mapbefine.mapbefine.common.interceptor;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.auth.infrastructure.AuthorizationExtractor;
import com.mapbefine.mapbefine.auth.infrastructure.JwtTokenProvider;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;
import com.mapbefine.mapbefine.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final String UNAUTHORIZED_ERROR_MESSAGE = "로그인에 실패하였습니다.";
    private static final ErrorCode ILLEGAL_TOKEN = new ErrorCode("03101", UNAUTHORIZED_ERROR_MESSAGE);
    private static final ErrorCode FORBIDDEN_ADMIN_ACCESS = new ErrorCode("03102", UNAUTHORIZED_ERROR_MESSAGE);

    private final AuthorizationExtractor<AuthInfo> authorizationExtractor;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthInterceptor(
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
        String accessToken = authInfo.accessToken();
        if (jwtTokenProvider.validateToken(accessToken)) {
            return Long.parseLong(jwtTokenProvider.getPayload(accessToken));
        }
        throw new UnauthorizedException(ILLEGAL_TOKEN);
    }

    private void validateAdmin(Long memberId) {
        if (authService.isAdmin(memberId)) {
            return;
        }

        throw new ForbiddenException(FORBIDDEN_ADMIN_ACCESS);
    }

}
