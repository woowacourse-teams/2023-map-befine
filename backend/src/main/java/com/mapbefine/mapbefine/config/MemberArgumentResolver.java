package com.mapbefine.mapbefine.config;

import com.mapbefine.mapbefine.entity.Member;
import com.mapbefine.mapbefine.entity.Role;
import com.mapbefine.mapbefine.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";
    private static final Member GUEST = new Member("게스트", "guest@naver.com", "image.url", Role.GUEST); // Guest 객체는 따로 만드는 것이 좋을까?

    private final MemberRepository memberRepository;

    public MemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType()
                .equals(Member.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String memberEmail = request.getHeader(AUTHORIZATION);
        Member member = memberRepository.findByEmail(memberEmail).orElse(null);

        return AuthMember.from(member); // 이런 식으로 하면 바로 Guest, User, Admin 이 가능하지 않을까
    }
}
