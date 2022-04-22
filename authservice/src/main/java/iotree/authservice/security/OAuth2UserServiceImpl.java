package iotree.authservice.security;

import iotree.authservice.mapper.AuthMapper;
import iotree.authservice.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public static final String USER_KEY_NAME = "userVo";

    @Value("${auth.default-role: ROLE_USER}")
    private String defaultRole;

    @Autowired
    private AuthMapper authMapper;

    private static final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = loadInterestingAttributes(userRequest);
        UserVo userVo = getUserVo(attributes);
        List<GrantedAuthority> authorities = getAuthorities(userVo);

        // OAuth2user 객체를 반환해야한다. success handler의 authentication 파라미터의 principal에 여기서 반환된 객체가 들어있다.
        return new DefaultOAuth2User(
                authorities,
                Collections.singletonMap(USER_KEY_NAME, userVo),
                USER_KEY_NAME // key가 하나라서 별 의미없다.
        ); // 여기서 반환된 객체가 Authentication 의 principal 이다.
    }

    /**
     * OAuth Provider마다 제공하는 정보의 종류가 다르다. 여기서 각 Provider 별로 필요한 정보를 뽑아서
     * Map으로 반환한다. 사용자에게 공개되는 정보는 Provider의 설정에서 결정할 수 있다.
     * 반환되는 Map 객체를 생성할 때는 내부적으로 통일된 key 값을 사용해서 공통으로 사용할 수
     * 있도록 해야한다.
     *
     * @param userRequest OAuth2UserRequest 객체. loadUser()의 파라미터로 넘어온다.
     * @return 각 Provider 로부터 뽑아낸 attribute 들을 담은 Map 객체.
     */
    private Map<String, Object> loadInterestingAttributes(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest); // OAuth2 Provider의 사용자 정보 로딩.

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // sub
        Map<String, Object> attributes = oAuth2User.getAttributes();

        HashMap<String, Object> newAttributes = new HashMap<>();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        switch (registrationId) {
            case "google":
                // - attributes: sub, name, given_name, family_name, picture, email, email_verified, locale, hd
                // - nameAttributeKey: sub
                newAttributes.put("oauth_key", attributes.get(userNameAttributeName)); // 현재 사용하지 않기 때문에 별 의미는 없다.

                newAttributes.put("email", attributes.get("email"));
                newAttributes.put("name", attributes.get("name"));
                return newAttributes;
            case "naver":
                // - attributes: id, email, name, profile_image
                // - nameAttributeKey: response ('response'키의 값에 map 객체가 있고 그 안에 위 attribute 들이 있다.)
                @SuppressWarnings("unchecked") Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                newAttributes.put("oauth_key", response.get("id")); // 현재 사용하지 않기 때문에 별 의미는 없다.

                newAttributes.put("email", response.get("email"));
                newAttributes.put("name", response.get("name"));
                return newAttributes;
            case "kakao":
                // - attributes: scope에 지정된 이름들과 다르다. 디버거로 찍어보고 적절하게 뽑아내야 한다.
                // - nameAttributeKey: id
                Long id = (Long) attributes.get(userNameAttributeName);
                @SuppressWarnings("unchecked") Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
                @SuppressWarnings("unchecked") Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

                newAttributes.put("oauth_key", id.toString()); // 현재 사용하지 않기 때문에 별 의미는 없다.

                newAttributes.put("email", kakaoAccount.get("email"));
                newAttributes.put("name", properties.get("nickname"));

                return newAttributes;
            default:
                throw new OAuth2AuthenticationException("Unknown OAuth provider. [" + registrationId + "]");
        }
    }

    /**
     * OAuth의 정보로부터 뽑아낸 정보를 이용하여 UserVo객체를 생성해서 반환한다.
     * 새로운 사용자라면 DB에 새로 등록을 하고 이미 등록된 사용자라면 DB에서 필요한 정보를 가져와서
     * UserVo 객체를 생성/반환해야 한다.
     * UserVo 객체는 반드시 DB의 primary key 값을 가지고 있어야 한다.
     *
     * @param attributes OAuth 로부터 뽑아낸 공통 속성을 담은 map 객체.
     * @return OAuth 정보로부터 생성된 UserVo객체.
     */
    @Transactional
    private UserVo getUserVo(Map<String, Object> attributes) {
        String id = (String) attributes.get("email");
        UserVo userVo = authMapper.getUserById(id);
        if (userVo == null) { // new user
            userVo = new UserVo();
            userVo.setId(id); // 새로 등록할 때 반드시 필요한 정보들 설정.
            userVo.setName((String) attributes.get("name"));
            authMapper.createOAuthUser(userVo);
            authMapper.createUserData(userVo);
            authMapper.addAuthority(userVo.getId(), defaultRole);
        }
        return userVo;
    }

    /**
     * 해당 사용자의 authority 목록을 반환한다.
     *
     * @param userVo authority를 조회할 사용자 정보를 담은 UserVo 객체
     * @return authority 리스트.
     */
    private List<GrantedAuthority> getAuthorities(UserVo userVo) {
        List<String> roles = authMapper.getUserAuthorities(userVo.getId());
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
