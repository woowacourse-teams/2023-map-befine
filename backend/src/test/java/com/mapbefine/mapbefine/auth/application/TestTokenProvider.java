package com.mapbefine.mapbefine.auth.application;

import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.EXPIRED_TOKEN;
import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.ILLEGAL_TOKEN;

import com.mapbefine.mapbefine.auth.exception.AuthException.AuthUnauthorizedException;
import com.mapbefine.mapbefine.auth.infrastructure.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class TestTokenProvider implements TokenProvider {

    private static final String EMPTY = "";

    private final String secretKey;
    private final Date issuedAt;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;

    public TestTokenProvider(
            String secretKey,
            Date issuedAt,
            long accessExpirationTime,
            long refreshExpirationTime
    ) {
        this.secretKey = secretKey;
        this.issuedAt = issuedAt;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    @Override
    public String createAccessToken(String payload) {
        return createToken(payload, new Date(), accessExpirationTime);
    }

    @Override
    public String createRefreshToken() {
        return createToken(EMPTY, new Date(), refreshExpirationTime);
    }

    public String createExpiredAccessToken(String payload) {
        return createToken(payload, issuedAt, accessExpirationTime);
    }

    private String createToken(String payload, Date issuedAt, Long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date validity = new Date(issuedAt.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String getPayload(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public void validateTokensForReissue(String refreshToken, String accessToken) {
        boolean canReissueAccessToken = !isExpired(refreshToken) && isExpired(accessToken);
        if (!canReissueAccessToken) {
            throw new AuthUnauthorizedException(ILLEGAL_TOKEN);
        }
    }

    @Override
    public void validateTokensForRemoval(String refreshToken, String accessToken) {
        boolean canRemoveRefreshToken = !isExpired(refreshToken) && !isExpired(accessToken);
        if (!canRemoveRefreshToken) {
            throw new AuthUnauthorizedException(EXPIRED_TOKEN);
        }
    }

    @Override
    public void validateAccessToken(String accessToken) {
        if (isExpired(accessToken)) {
            throw new AuthUnauthorizedException(EXPIRED_TOKEN);
        }
    }

    private boolean isExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            Date expiration = claims.getBody().getExpiration();

            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthUnauthorizedException(ILLEGAL_TOKEN);
        }
    }
}
