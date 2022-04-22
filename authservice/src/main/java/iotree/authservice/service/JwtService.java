package iotree.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private static final String COOKIE_NAME = "jwt";

    public String generateJwtToken(String subject, Date expiration, Map<String, Object> claims, SecretKey secretKey) {
        // 토큰을 생성해서 반환.
        return Jwts.builder()
                .setSubject(subject) // JWT의 'sub'ject claim에 uid 정보 저장
                .setExpiration(expiration) // 토큰 만료 시간 설정.
                .addClaims(claims) // 커스텀 정보 추가.
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public void addJwtToResponseCookie(String jwt, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // 반드시 명확하게 지정할 것.
        response.addCookie(cookie);
    }

    public void removeJwtFromResponseCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // 반드시 명확하게 지정할 것.
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
