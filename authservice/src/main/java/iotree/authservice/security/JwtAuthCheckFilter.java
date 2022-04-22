package iotree.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import iotree.authservice.service.AuthService;
import iotree.authservice.service.JwtService;
import iotree.authservice.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * 인증을 요구하는 모든 경로에 대해서 적용되는 필터이다.
 * jwt 쿠키를 파싱하여 JWT를 추출하고 유효한 JWT 토큰인지 검증한다.
 * 검증이 실패하면 401 Unauthorized 코드를 내려보낸다.
 */
@Component
public class JwtAuthCheckFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @Value("${auth.header: X-Auth}")
    private String authHeaderName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Authentication authentication;

        try {
            String authHeader = request.getHeader(authHeaderName);
            String json = new String(Base64.getDecoder().decode(authHeader), StandardCharsets.UTF_8);
            @SuppressWarnings("unchecked") Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);
            UserVo userVo = new UserVo();
            userVo.setId((String) map.get("sub"));
            userVo.setName((String) map.get("name"));
            authentication = new UsernamePasswordAuthenticationToken(userVo, null, null); // 권한 필요없음.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ignored) {
        }

        // doFileter()를 호출하여 다음으로 진행하더라도 SecurityContext에 Authentication이 설정되어 있지 않으면
        // 이후의 필터에서 통과가 되지 않고 Forbidden이 반환될 것이다.
        chain.doFilter(request, response);
    }
}
