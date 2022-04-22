package iotree.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import iotree.authservice.dto.LoginReqDto;
import iotree.authservice.dto.LoginRespDto;
import iotree.authservice.mapper.AuthMapper;
import iotree.authservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    // 만료 시간은 1일 이지만 브라우저를 다시 시작하면 재인증해야 하므로 문제없다.
    private static final long JWT_EXPIRATION_TIME = 86_400_000; // 1 day

    private static final String SECRET = "Ke5r!tW*teiif3xm6d#nh_afeijfv39ru!68h-r!ewijfsajif8ewjh5dw2m2dwk"; // 길이 64짜리 random 문자열을 주도록하자. alphabet, number, special char, ...
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private AuthMapper authMapper;

    @Override
    public Authentication authenticate(LoginReqDto loginReqDto) {
        UserVo userVo = authMapper.getUserById(loginReqDto.getUserId());
        if (userVo != null) {
            if (PASSWORD_ENCODER.matches(loginReqDto.getPassword(), userVo.getPassword())) {
                List<String> roles = authMapper.getUserAuthorities(userVo.getId());
                List<? extends GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                userVo.erasePersonalInfo();
                return new UsernamePasswordAuthenticationToken(userVo, null, authorities);
            } else {
                throw new BadCredentialsException("Wrong password.");
            }
        } else {
            throw new BadCredentialsException("No such user.");
        }
    }

    @Override
    public LoginRespDto getSuccessfulAuthResponseBody(Authentication authentication) {
        LoginRespDto loginRespDto = new LoginRespDto();
        loginRespDto.setCode(0);
        loginRespDto.setUser((UserVo) authentication.getPrincipal());
        return loginRespDto;
    }

    @Override
    public LoginRespDto getUnsuccessfulAuthResponseBody(Exception cause) {
        LoginRespDto loginRespDto = new LoginRespDto();
        if (cause instanceof BadCredentialsException) {
            loginRespDto.setCode(-1); // invalid id or password
        } else {
            loginRespDto.setCode(-99); // unknown
        }
        loginRespDto.setMessage(cause.getLocalizedMessage());
        return loginRespDto;
    }

    @Override
    public SecretKey getJwtSecret() {
        return KEY;
    }

    @Override
    public long getJwtExpiration() {
        return JWT_EXPIRATION_TIME;
    }

    @Override
    public String getJwtSubject(Authentication authentication) {
        // JWT 토큰의 subject로 저장할 값을 반환한다.
        UserVo userVo = (UserVo) authentication.getPrincipal();
        return userVo.getId();
    }

    @Override
    public Map<String, Object> getJwtClaims(Authentication authentication) {
        UserVo userVo = (UserVo) authentication.getPrincipal();

        // 사용자 이름 (많은 page에서 표시되는 정보라면 이렇게 claims에 넣어놓으면 효율적이다)
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userVo.getName());

        // 사용자 권한 (authority 정보도 매번 DB를 통해서 받아오는 건 비효율적이므로 claims에 넣어놓는다)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream().map(Object::toString).collect(Collectors.toList());
        claims.put("authorities", roles);

        return claims;
    }

    @Override
    public Authentication getAuthenticationFromJwt(Claims claims) {
        UserVo userVo = new UserVo();

        // JWT에서 사용자 정보 복원.
        userVo.setId(claims.getSubject());
        userVo.setName(claims.get("name").toString());

        // JWT에서 권한 정보 복원.
        @SuppressWarnings("unchecked")
        Collection<String> roles = (Collection<String>) claims.get("authorities");
        List<? extends GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userVo, null, authorities);
    }
}
