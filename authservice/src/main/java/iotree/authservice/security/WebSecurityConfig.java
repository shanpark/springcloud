package iotree.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security 설정 클래스.
 * JWT를 위한 기본 설정이 되어있다. 추가 설정이 필요한 경우 여기서 적용해줘야 한다.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthCheckFilter jwtAuthCheckFilter;
    @Autowired
    private OAuth2UserServiceImpl oAuth2UserServiceImpl;
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired
    private OAuth2FailureHandler oAuth2FailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // JWT 인증을 사용하는 REST API를 구현할 것이므로 CSRF를 disable 시켜도 좋다.
        http.csrf().disable();

        // Vue.js가 생성하는 파일들에 대한 요청은 모두 허가해줘야 한다. 정적 데이터이므로 모두 허가해줘도 문제가 없다.
        // '/rest/auth'는 인증 요청이므로 모두 허용이고 나머지 REST api들은 인증 사용자만 access 가능하도록 설정한다.
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/actuator/**", "/css/**", "/js/**", "/fonts/**", "/icons/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .anyRequest().authenticated();

        http.formLogin().disable();

        // OAuth의 필터들은 아래 필터들보다 앞이다. 따라서 OAuth 관련 요청들은 아래 필터에 도달하기 전에 먼저 처리되는 것으로 판단된다.
        http.oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint().userService(oAuth2UserServiceImpl);

        // 나머지 요청에 대한 필터 설정.
        http.addFilterBefore(jwtAuthCheckFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
