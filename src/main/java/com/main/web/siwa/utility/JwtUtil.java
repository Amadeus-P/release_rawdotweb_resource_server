package com.main.web.siwa.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.main.web.siwa.entity.SiwaUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    // 나만의 비밀키
    private final Key secretKey;

    // final 에러는 오버로드 생성자로 구현해 주면 됨
    public JwtUtil(@Value("${siwa.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // 유효성 검사를 위해 클레임을 추출
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public List<String> extractRoles(String token) {
        List<Map<String, String>> roles = extractAllClaims(token).get("roles", List.class);

        List<String> roleNames = new ArrayList<>();
        for(Map<String, String> role : roles) {
            System.out.println(role);
            roleNames.add(role.get("authority"));
            System.out.println(role.get("authority"));
        }
        return roleNames;
    }

    public Long extractId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // 내가 비밀키를 넣어줌
                .build() // 인증이 올바르지않으면 에러가 남
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(SiwaUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("username", userDetails.getUsername());
        claims.put("profileName", userDetails.getProfileName());
        claims.put("profileImage", userDetails.getProfileImage());
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }
}