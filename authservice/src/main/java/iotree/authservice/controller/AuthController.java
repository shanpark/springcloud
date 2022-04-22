package iotree.authservice.controller;

import iotree.authservice.dto.LoginReqDto;
import iotree.authservice.dto.LoginRespDto;
import iotree.authservice.dto.LogoutReqDto;
import iotree.authservice.dto.LogoutRespDto;
import iotree.authservice.service.AuthService;
import iotree.authservice.service.JwtService;
import iotree.authservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/login")
    public LoginRespDto authLogin(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        LoginRespDto loginRespDto;

        try {
            Authentication authentication = authService.authenticate(loginReqDto);

            // JWT 토큰을 생성한다.
            String jwt = jwtService.generateJwtToken(
                    authService.getJwtSubject(authentication),
                    new Date(System.currentTimeMillis() + authService.getJwtExpiration()),
                    authService.getJwtClaims(authentication),
                    authService.getJwtSecret()
            );

            // 생성된 토큰을 httpOnly Cookie에 추가한다.
            jwtService.addJwtToResponseCookie(jwt, response);

            loginRespDto = authService.getSuccessfulAuthResponseBody(authentication);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            loginRespDto = authService.getUnsuccessfulAuthResponseBody(e);
        }

        return loginRespDto;
    }

    @GetMapping("/auth/relogin")
    public LoginRespDto authRelogin(HttpServletRequest request) {
        LoginRespDto loginRespDto;

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            loginRespDto = authService.getSuccessfulAuthResponseBody(authentication);
        } catch (Exception e) {
            loginRespDto = authService.getUnsuccessfulAuthResponseBody(e);
        }

        return loginRespDto;
    }

    @PostMapping("/auth/logout")
    public LogoutRespDto authLogout(@RequestBody LogoutReqDto logoutReqDto, HttpServletRequest request, HttpServletResponse response) {
        LogoutRespDto logoutRespDto = new LogoutRespDto();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserVo userVo = (UserVo) authentication.getPrincipal();
            if (Objects.equals(userVo.getId(), logoutReqDto.getId())) {
                jwtService.removeJwtFromResponseCookie(response);
                SecurityContextHolder.clearContext();
            } else {
                logoutRespDto.setCode(-1); // 요청 id가 유효하지 않음.
            }
        } catch (Exception e) {
            log.error("Exception occurred in AuthController.authLogout().", e);
            logoutRespDto.setCode(-1); // 요청 id가 유효하지 않음.
        }

        return logoutRespDto;
    }
}
