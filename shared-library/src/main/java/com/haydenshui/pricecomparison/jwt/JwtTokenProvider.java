package com.haydenshui.pricecomparison.shared.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.haydenshui.pricecomparison.shared.exception.custom.JwtExpiredException;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private String secretKey;

    private long expiration;

    public JwtTokenProvider(String secretKey, long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }

    // 使用 java.security.Key 来管理密钥
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = parseClaims(token);
        return (List<String>) claims.get("roles");
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration();
    }

    private Claims parseClaims(String token) {
        try{
            return Jwts.parser()
                .setSigningKey(getSigningKey())  // 设置签名密钥
                .build()
                .parseSignedClaims(token)  // 使用新的方法 parseSignedClaims
                .getBody();  // 返回 Claims 对象
        } catch(io.jsonwebtoken.ExpiredJwtException e) {
            throw new JwtExpiredException("JWT token has expired");
        }
    }
}
