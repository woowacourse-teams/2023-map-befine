package com.mapbefine.mapbefine.auth.infrastructure;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class BearerAuthorizationExtractor implements AuthorizationExtractor<AuthInfo> {

    private static final String BEARER_TYPE = "Bearer";

    @Override
    public AuthInfo extract(HttpServletRequest request) {
        return Collections.list(request.getHeaders(AUTHORIZATION))
                .stream()
                .filter(this::isBearerType)
                .map(this::extractAuthHeaderValue)
                .findFirst()
                .map(AuthInfo::new)
                .orElse(null);
    }

    private boolean isBearerType(String value) {
        return value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
    }

    private String extractAuthHeaderValue(String value) {
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();

        return authHeaderValue;
    }

}
