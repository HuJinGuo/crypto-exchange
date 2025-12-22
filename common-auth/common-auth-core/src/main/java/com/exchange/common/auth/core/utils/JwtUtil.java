package com.exchange.common.auth.core.utils;

import com.exchange.common.auth.core.constant.AuthConstants;
import com.exchange.common.auth.core.context.UserContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public final class JwtUtil {

    private JwtUtil() {}

    private static Key key(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public static String createToken(UserContext ctx, String secret, long expireSeconds) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date exp = new Date(now + expireSeconds * 1000);

        return Jwts.builder()
                .setIssuedAt(issuedAt)
                .setExpiration(exp)
                .claim(AuthConstants.CLAIM_USER_ID, ctx.getUserId())
                .claim(AuthConstants.CLAIM_USERNAME, ctx.getUserName())
                .signWith(key(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseClaims(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(key(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static UserContext parseUser(String token, String secret) {
        Claims claims = parseClaims(token, secret);
        Object uid = claims.get(AuthConstants.CLAIM_USER_ID);
        Object un = claims.get(AuthConstants.CLAIM_USERNAME);

        Long userId = uid == null ? null : Long.valueOf(String.valueOf(uid));
        String username = un == null ? null : String.valueOf(un);

        return new UserContext(userId, username);
    }

    public static boolean isExpired(String token, String secret) {
        try {
            Claims claims = parseClaims(token, secret);
            Date exp = claims.getExpiration();
            return exp != null && exp.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}