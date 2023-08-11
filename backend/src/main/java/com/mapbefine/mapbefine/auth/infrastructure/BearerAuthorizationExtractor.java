package com.mapbefine.mapbefine.auth.infrastructure;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.auth.dto.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.springframework.stereotype.Component;

@Component
public class BearerAuthorizationExtractor implements AuthorizationExtractor<AuthInfo> {
    private static final String BEARER_TYPE = "Bearer";

    @Override
    public AuthInfo extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return new AuthInfo(authHeaderValue);
            }
        }

        return null;
    }
}
