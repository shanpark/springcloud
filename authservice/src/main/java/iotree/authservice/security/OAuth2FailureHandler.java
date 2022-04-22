package iotree.authservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.warn("OAuth failed. [{}]", exception.getLocalizedMessage());
        // TODO
        //  OAuth 수행 도중에 예상치 못한 exception이 발생하면 여기로 올 것이다.
        //  아무 것도 처리하지 않으면 GlobalErrorController로 넘어가서 처리가 되고 거기에서
        //  index.html이 반환되므로 인증화면으로 다시 돌아갈 것이다.
        //  만약 다른 처리가 필요하다면 여기서 redirect 시켜서 처리해야 할 것 같다.
    }
}
