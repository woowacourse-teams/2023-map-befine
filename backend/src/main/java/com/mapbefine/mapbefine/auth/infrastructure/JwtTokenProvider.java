package com.mapbefine.mapbefine.auth.infrastructure;

import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.BAD_REQUEST_TOKEN;
import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.EXPIRED_TOKEN;
import static com.mapbefine.mapbefine.auth.exception.AuthErrorCode.ILLEGAL_TOKEN;

import com.mapbefine.mapbefine.auth.exception.AuthException.AuthUnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

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
        UUID payload = UUID.randomUUID();

        return createToken(payload.toString(), refreshExpirationTime);
    }

    private String createToken(String payload, Long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

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
        if (canReissueAccessToken) {
            return;
        }
        throw new AuthUnauthorizedException(ILLEGAL_TOKEN);
    }

    public void validateTokensForRemoval(String refreshToken, String accessToken) {
        boolean canRemoveRefreshToken = !isExpired(refreshToken) && !isExpired(accessToken);
        if (canRemoveRefreshToken) {
            return;
        }
        throw new AuthUnauthorizedException(BAD_REQUEST_TOKEN);
    }

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

