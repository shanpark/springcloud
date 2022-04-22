package iotree.authservice.security;

import iotree.authservice.service.AuthService;
import iotree.authservice.service.JwtService;
import iotree.authservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;

    @Value("${auth.success-url: http://localhost:8080/}")
    private String authSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Success Handler called.");
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        UserVo userVo = defaultOAuth2User.getAttribute(OAuth2UserServiceImpl.USER_KEY_NAME);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userVo, null, defaultOAuth2User.getAuthorities());

        // JWT 토큰을 생성하고 생성된 토큰을 httpOnly Cookie에 추가한다.
        String jwt = jwtService.generateJwtToken(
                authService.getJwtSubject(usernamePasswordAuthenticationToken),
                new Date(System.currentTimeMillis() + authService.getJwtExpiration()),
                authService.getJwtClaims(usernamePasswordAuthenticationToken),
                authService.getJwtSecret()
        );

        jwtService.addJwtToResponseCookie(jwt, response);

        // 인증 완료 후 default redirect url은 '/login/oauth2/code/*' 이기 때문에 주소창에 '/login/oauth2/code/*' 로
        // 이동된 채로 완료가 된다. 따라서 적당한 url을 authSuccessUrl 에 지정해 주고 이동하도록 해야 한다.
        // 이 때 지정되는 URL은 반드시 요청이 시작된 URL과 같은 domain이어야 한다. 그렇지 않으면 설정된 cookie를 찾을 수 없어서
        // 'authorization_request_not_found' 에러가 나면서 실패 처리되는 걸 볼 수 있다.
        response.sendRedirect(authSuccessUrl);
    }
}
