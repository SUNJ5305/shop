package sunjin.com.shop.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long TOKEN_EXPIRATION_TIME_MS = 1000 * 60 * 60; // 1시간

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    /**
     * 초기화 시 서명 키를 생성한다.
     */
    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 사용자 이름으로 JWT 토큰을 생성한다.
     * @param username 사용자 이름 (이메일)
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_MS))
                .signWith(signingKey)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름을 추출한다.
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsernameFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    /**
     * JWT 토큰의 유효성을 검증한다.
     * @param token 검증할 JWT 토큰
     * @return 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}