package com.mapbefine.mapbefine.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = JwtTokenProvider.class)
@TestPropertySource(locations = "classpath:application.yml")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("payload를 받아 access token을 생성한다.")
    void createAccessToken_success() {
        String payload = "1";

        String token = jwtTokenProvider.createAccessToken(payload);

        assertThat(jwtTokenProvider.getPayload(token))
                .isEqualTo(payload);
    }

}
