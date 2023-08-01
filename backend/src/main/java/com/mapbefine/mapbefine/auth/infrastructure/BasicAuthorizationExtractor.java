package com.mapbefine.mapbefine.auth.infrastructure;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthorizationExtractor implements AuthorizationExtractor<AuthInfo> {

    private static final String BASIC_TYPE = "Basic";

    @Override
    public AuthInfo extract(final HttpServletRequest request) {
        String requestHeader = request.getHeader(AUTHORIZATION);
        String authorization = decode(requestHeader);

        validateAuthorization(authorization);
        String trim = authorization.substring(BASIC_TYPE.length()).trim();

        return new AuthInfo(trim);
    }

    private void validateAuthorization(final String authorization) {
        if (!hasText(authorization)) {
            throw new IllegalArgumentException("로그인 유저가 아닙니다");
        }
        if (!authorization.toLowerCase().startsWith(BASIC_TYPE.toLowerCase())) {
            throw new IllegalArgumentException("잘못된 인증방법입니다.");
        }
    }

    private String decode(String authorization) {
        return new String(Base64.decodeBase64(authorization));
    }

}
