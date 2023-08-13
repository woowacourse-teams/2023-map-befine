package com.mapbefine.mapbefine.common.resolver;

import com.mapbefine.mapbefine.auth.application.AuthService;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AuthMember.class.equals(parameter.getParameterType());
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
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("정상적인 요청이 아닙니다.");
        }
    }

    private AuthMember createAuthMember(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        if (Objects.isNull(memberId)) {
            return new Guest();
        }

        return authService.findAuthMemberByMemberId(memberId);
    }

}
