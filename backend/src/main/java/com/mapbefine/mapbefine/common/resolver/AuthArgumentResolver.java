package com.mapbefine.mapbefine.common.resolver;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import com.mapbefine.mapbefine.auth.infrastructure.AuthorizationExtractor;
import com.mapbefine.mapbefine.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor<AuthInfo> authorizationExtractor;
    private final AuthService authService;

    public AuthArgumentResolver(
            AuthorizationExtractor<AuthInfo> authorizationExtractor,
            AuthService authService
    ) {
        this.authorizationExtractor = authorizationExtractor;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isAuthMemberAnnotation = parameter.hasParameterAnnotation(Auth.class);
        boolean isAuthMemberClass = AuthMember.class.equals(parameter.getParameterType());

        return isAuthMemberAnnotation && isAuthMemberClass;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        validateRequest(request);

        return createAuthMember(request);
    }

    private void validateRequest(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("로그인 유저가 아닙니다");
        }
    }

    private AuthMember createAuthMember(HttpServletRequest request) {
        AuthInfo authInfo = authorizationExtractor.extract(request);
        Member member = authService.checkLoginMember(authInfo);

        return AuthMember.from(member);
    }
}
