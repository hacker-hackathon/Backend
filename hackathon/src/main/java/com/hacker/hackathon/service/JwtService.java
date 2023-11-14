package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.controller.exception.UnauthorizedException;
import com.hacker.hackathon.dto.authorization.response.TokenServiceVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.time.Duration.ofDays;
import static java.time.Duration.ofMinutes;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    private static final Long ACCESS_TOKEN_VALID_TIME = ofDays(10).toMillis();
    private static final Long REFRESH_TOKEN_VALID_TIME = ofDays(14).toMillis();


    @PostConstruct // 한번만 생성되도록 하는
    protected void init() {
        jwtSecret = Base64.getEncoder()
            .encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(Long usersId, Long tokenValidTime, String tokenType) {
        Claims claims = Jwts.claims()
            .setSubject(tokenType);

        claims.put("usersId", usersId);

        return Jwts.builder()
            .setClaims(claims)
            .setHeaderParam("type", tokenType)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(HS256, jwtSecret.getBytes())
            .compact();
    }

    public TokenServiceVO createServiceToken(Long userId) {
        return TokenServiceVO.builder()
            .accessToken(createAccessToken(userId))
            .refreshToken(createRefreshToken(userId))
            .build();
    }

    public String createAccessToken(Long userId) {
        return createJwt(userId, ACCESS_TOKEN_VALID_TIME, ACCESS_TOKEN);
    }

    public String createRefreshToken(Long userId) {
        return createJwt(userId, REFRESH_TOKEN_VALID_TIME, REFRESH_TOKEN);
    }

    public boolean isExpired(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return true;
        }

        return false;
    }

    public boolean verifyToken(String token) {
        System.out.println("tokenis:"+ token);
        try {
            final Claims claims = getBody(token);
            System.out.println("tokenclaimes"+claims);
            return true;
        } catch (RuntimeException e) {
            if (e instanceof ExpiredJwtException) {
                throw new UnauthorizedException(ErrorMessage.EXPIRED_TOKEN);
            }
            return false;
        }
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Long getUserId(String token) {
        try {
            // Remove 'Bearer ' prefix if it exists
            token = token.startsWith("Bearer ") ? token.substring(7) : token;

            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build();

            Claims claims = parser.parseClaimsJws(token).getBody();
            System.out.println("Claims: " + claims);

            Object userIdObj = claims.get("usersId");
            if (userIdObj == null) {
                throw new IllegalArgumentException("User ID not found in token");
            }
            return Long.valueOf(userIdObj.toString());
        } catch (JwtException e) {
            // Log the exception details for debugging
            System.err.println("Error parsing JWT: " + e.getMessage());
            throw e;
        }
    }


    public String getJwtContents(String token) {
        final Claims claims = getBody(token);
        Object userId = claims.get("usersId");
        System.out.println("getJwtContents"+userId);
        return userId != null ? userId.toString() : null;
    }
}
