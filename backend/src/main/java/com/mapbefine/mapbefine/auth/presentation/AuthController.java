package com.mapbefine.mapbefine.auth.presentation;

import com.mapbefine.mapbefine.auth.application.TokenService;
import com.mapbefine.mapbefine.auth.dto.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessToken> reissueAccessToken(
            @CookieValue("refresh-token") String refreshToken,
            @RequestBody AccessToken request
    ) {
        AccessToken accessToken = tokenService.reissueAccessToken(refreshToken, request.accessToken());

        return ResponseEntity.ok(accessToken);
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
