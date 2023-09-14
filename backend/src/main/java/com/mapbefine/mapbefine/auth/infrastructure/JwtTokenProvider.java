package com.mapbefine.mapbefine.auth.infrastructure;

import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.EXPIRED_TOKEN;
import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.ILLEGAL_TOKEN;

import com.mapbefine.mapbefine.auth.exception.AuthException.AuthUnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String EMPTY = "";

    private final String secretKey;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}")
            String secretKey,
            @Value("${security.jwt.token.access-expire-length}")
            long accessExpirationTime,
            @Value("${security.jwt.token.refresh-expire-length}")
            long refreshExpirationTime
    ) {
        this.secretKey = secretKey;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public String createAccessToken(String payload) {
        return createToken(payload, accessExpirationTime);
    }

    public String createRefreshToken() {
        return createToken(EMPTY, refreshExpirationTime);
    }

    private String createToken(final String payload, final Long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(payload);
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public void validateTokensForReissue(String refreshToken, String accessToken) {
        boolean canReissueAccessToken = !isExpired(refreshToken) && isExpired(accessToken);
        if (!canReissueAccessToken) {
            throw new AuthUnauthorizedException(ILLEGAL_TOKEN);
        }
    }

    public void validateTokensForRemoval(String refreshToken, String accessToken) {
        boolean canRemoveRefreshToken = !isExpired(refreshToken) && !isExpired(accessToken);
        if (!canRemoveRefreshToken) {
            throw new AuthUnauthorizedException(EXPIRED_TOKEN);
        }
    }

    public void validateAccessToken(String accessToken) {
        if (isExpired(accessToken)) {
            throw new AuthUnauthorizedException(EXPIRED_TOKEN);
        }
    }

    private boolean isExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthUnauthorizedException(ILLEGAL_TOKEN);
        }
    }

}

