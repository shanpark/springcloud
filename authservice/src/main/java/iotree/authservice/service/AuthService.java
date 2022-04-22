package iotree.authservice.service;

import io.jsonwebtoken.Claims;
import iotree.authservice.dto.LoginReqDto;
import iotree.authservice.dto.LoginRespDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;

/**
 * JWT 인증 지원을 위해서 AuthService 인터페이스를 구현해야한다. (AuthServiceImpl.java 참조)
 * 기본 제공 API는 다음과 같다.
 *   - /auth/login => 인증 요청. 인증된 사용자의 인증 정보를 내려준다.
 *   - /auth/relogin => 현재 인증된 사용자의 인증 정보 재요청.
 *   - /aith/logout => 현재 인증된 사용자의 logout 요청.
 */
@Service
public interface AuthService {
    /**
     * LoginReqDto 객체를 받아서 인증을 수행하고 인증이 성공적인 경우 Authentication 객체를 생성하여 반환한다.
     *
     * @param loginReqDto 인증을 수행하기위한 정보를 담고 있는 객체. 규격에 따라 다르지만 일반적으로 userId, password 같은 정보를
     *                   담고 있다.
     * @return 인증이 성공적인 경우 적절한 Authentication 객체를 반환한다. 일반적으로 UsernamePasswordAuthenticationToken 객체를
     *         생성하여 반환한다.
     * @throws BadCredentialsException 인증이 실패했을 때 던져지는 Exception.
     */
    Authentication authenticate(LoginReqDto loginReqDto);

    /**
     * 규격에 따라 인증이 성공했을 때 응답의 Body로 내려보낼 LoginRespDto 객체를 생성해서 반환한다.
     *
     * @param authentication 위의 authentication() 메소드가 반환한 Authentication 객체이다. 응답 객체를 작성할 때 필요한
     *                       정보가 있으면 사용한다.
     * @return '/rest/auth' 요청의 응답의 body로 내려가는 LoginRespDto 객체.
     */
    LoginRespDto getSuccessfulAuthResponseBody(Authentication authentication);

    /**
     * 규격에 따라 인증이 실했을 때 응답의 Body로 내려보낼 LoginRespDto 객체를 생성해서 반환한다.
     *
     * @param cause 위의 authentcate() 메소드가 인증 실패 시 반환한 exception 객체.
     * @return '/rest/auth' 요청의 응답의 body로 내려가는 LoginRespDto 객체.
     */
    LoginRespDto getUnsuccessfulAuthResponseBody(Exception cause);

    /**
     * JWT 토큰 생성 시 암호화에 필요한 SecretKey를 반환한다.
     *
     * @return JWT 토큰 생성 시 사용되는 암호화 키값.
     */
    SecretKey getJwtSecret();

    /**
     * JWT 토큰의 만료 시간을 ms 단위로 반환한다.
     * 브라우저를 종료하지 않고 계속 사용한다면 만료시간까지 사용할 수 있다. 하지만 로그아웃을 하거나
     * 브라우저를 종료하면 재인증을 해야하므로 충분히 큰 값을 지정해도 괜찮다.
     *
     * @return JWT 토큰 만료 시간을 ms 단위로 반환.
     */
    long getJwtExpiration();

    /**
     * JWT 토큰의 subject 필드에 저장할 값을 반환한다.
     *
     * @param authentication 위의 authentication() 메소드가 반환한 Authentication 객체이다.
     * @return 파라미터로 받은 Authentication 객체에 저장된 사용자 식별자 값을 문자열로 반환한다.
     */
    String getJwtSubject(Authentication authentication);

    /**
     * JWT 토큰의 Claims 정보에 저장할 값들을 Map 객체로 반환한다.
     * subject에 저장되는 사용자 식별자값을 제외하고 자주 사용되는 사용자 정보를 저장해 놓으면 나중에 DB access 없이 값을
     * 사용할 수 있다. 사용자 권한, 이름, 직급 등 자주 사용하거나 표시되는 값들을 저장해 놓으면 좋다.
     *
     * @param authentication 위의 authentication() 메소드가 반환한 Authentication 객체이다.
     * @return 파라미터로 받은 Authentication 객체에 저장된 사용자 식별자 값을 문자열로 반환한다.
     */
    Map<String, Object> getJwtClaims(Authentication authentication);

    /**
     * JWT 토큰의 Claims 정보에서 Authentication 객체를 복원하여 반환한다.
     * 최초 인증 후에 후속 요청들은 쿠키에서 JWT 토큰에서 Authentication 객체를 복원하여 Security context에 설정해 주어야한다.
     * 복원된 Authentication 정보는 당연히 JWT의 Claims에 저장된 정보만을 가지고 있으므로 더 많은 정보가 필요한 경우에는
     * DB 등의 저장소를 통해서 가져와서 사용해야 한다.
     *
     * @param claims JWT를 parsing하여 뽑아낸 Claims 정보.
     * @return 전달된 claims 정보로부터 복원된 Authentication 객체.
     */
    Authentication getAuthenticationFromJwt(Claims claims);
}
