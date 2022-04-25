package iotree.authservice.controller;

import iotree.authservice.constants.ResultCode;
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

    /**
     * 사용자 인증을 요청하는 API 이다.
     */
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

    /**
     * 사용자 정보를 재요청한다.
     *
     * 응답으로 내려주는 정보는 사용자 식별을 위한 키값과 민감하지 않은 기본 정보만 내려준다.
     * 인증에 사용될 수 있는 비밀번호 같은 정보를 내려보내서는 안된다.
     *
     * 이미 인증되어 JWT 를 쿠키에 담고 있는 사용자가 인증 확인을 위해서 사용한다. 따라서 정상적으로 Controller까지
     * 인입되었다면 정상 사용자이며 에러가 나는 경우는 없을 것이다.
     */
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

    /**
     * Logout을 요청하는 API.
     *
     * 요청이 성공하면 SecurityContext 가 clear 되고 JWT 쿠키가 삭제된다.
     */
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
                logoutRespDto.setCode(ResultCode.INVALID_REQUEST.value()); // 요청 id가 유효하지 않음.
            }
        } catch (Exception e) {
            log.error("Exception occurred in AuthController.authLogout().", e);
            logoutRespDto.setCode(ResultCode.UNKNOWN_ERR.value());
        }

        return logoutRespDto;
    }
}
