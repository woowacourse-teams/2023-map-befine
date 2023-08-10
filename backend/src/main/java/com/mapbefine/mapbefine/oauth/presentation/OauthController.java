package com.mapbefine.mapbefine.oauth.presentation;

import com.mapbefine.mapbefine.oauth.KakaoToken;
import com.mapbefine.mapbefine.oauth.application.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/kakao")
    public ResponseEntity<Void> redirection(HttpServletResponse response) throws IOException {
        String redirectUrl = oauthService.getAuthCodeRequestUrl();
        response.sendRedirect(redirectUrl);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<KakaoToken> login(@RequestParam String code) {
        KakaoToken token = oauthService.fetch(code);

        return ResponseEntity.ok(token);
    }

}
