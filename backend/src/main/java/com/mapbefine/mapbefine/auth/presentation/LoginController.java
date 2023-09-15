package com.mapbefine.mapbefine.auth.presentation;

import com.mapbefine.mapbefine.auth.application.TokenService;
import com.mapbefine.mapbefine.auth.dto.AccessToken;
import com.mapbefine.mapbefine.auth.dto.LoginTokens;
import com.mapbefine.mapbefine.auth.dto.response.LoginInfoResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.oauth.application.OauthService;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    public static final int TWO_WEEKS = 14 * 24 * 60 * 60;
    private final TokenService tokenService;
    private final OauthService oauthService;

    public LoginController(TokenService tokenService, OauthService oauthService) {
        this.tokenService = tokenService;
        this.oauthService = oauthService;
    }

    @GetMapping("/oauth/{oauthServerType}")
    public ResponseEntity<Void> redirection(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) throws IOException {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth/login/{oauthServerType}")
    public ResponseEntity<LoginInfoResponse> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam String code,
            HttpServletResponse response
    ) {
        MemberDetailResponse memberDetail = oauthService.login(oauthServerType, code);

        LoginTokens loginTokens = tokenService.issueTokens(memberDetail.id());
        Cookie cookie = createCookie(loginTokens.refreshToken());
        response.addCookie(cookie);

        return ResponseEntity.ok(new LoginInfoResponse(loginTokens.accessToken(), memberDetail));
    }

    private Cookie createCookie(String refreshToken) {
        Cookie cookie = new Cookie("refresh-token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(TWO_WEEKS);
        return cookie;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessToken> reissueTokens(
            @CookieValue("refresh-token") String refreshToken,
            @RequestBody AccessToken request,
            HttpServletResponse response
    ) {
        LoginTokens loginTokens = tokenService.reissueToken(refreshToken, request.accessToken());
        Cookie cookie = createCookie(loginTokens.refreshToken());
        response.addCookie(cookie);

        return ResponseEntity.ok(loginTokens.toAccessToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("refresh-token") String refreshToken,
            @RequestBody AccessToken request
    ) {
        tokenService.removeRefreshToken(refreshToken, request.accessToken());

        return ResponseEntity.noContent().build();
    }

}
