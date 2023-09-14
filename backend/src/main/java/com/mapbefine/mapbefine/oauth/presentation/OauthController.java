package com.mapbefine.mapbefine.oauth.presentation;

import com.mapbefine.mapbefine.oauth.application.OauthService;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import com.mapbefine.mapbefine.oauth.dto.LoginInfoResponse;
import com.mapbefine.mapbefine.oauth.dto.LoginTokens;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final OauthService oauthService;

    public OauthController(final OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/{oauthServerType}")
    public ResponseEntity<Void> redirection(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) throws IOException {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{oauthServerType}")
    public ResponseEntity<LoginInfoResponse> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam String code,
            HttpServletResponse response
    ) {
        LoginTokens loginTokens = oauthService.login(oauthServerType, code);
        addRefreshTokenToCookie(response, loginTokens.refreshToken());

        return ResponseEntity.ok(loginTokens.toResponse());
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh-token", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
