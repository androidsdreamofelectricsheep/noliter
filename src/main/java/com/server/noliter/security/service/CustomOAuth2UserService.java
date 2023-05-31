package com.server.noliter.security.service;

import com.server.noliter.security.dto.SessionUser;
import com.server.noliter.security.oauth2.dto.OAuthAttributes;
import com.server.noliter.domain.user.User;
import com.server.noliter.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = save(oAuthAttributes);

        // new SessionUser 대신 그냥 User클래스를 사용한다면 직렬화를 구현하지 않았다는 에러가 발생
        // 그러면 직렬화를 구현하면 될까?
        // User클래스는 엔티티이기 때문에 다른 엔티티와 관계가 맺을 수 있음
        // @OneToMany, @ManyToMany 등 자식 엔티티를 갖고 있다면 직렬화 대상에 자식까지 포함되어
        // 성능 이슈, 부수 효과가 발생할 확률이 높아짐
        // 그래서 직렬화 기능을 가진 세션 DTO를 추가로 만들어 사용하는 것이 운영 및 유지보수에 도움이 됨
        httpSession.setAttribute("loginUser", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getNameAttributeKey()
        );
    }

    private User save(OAuthAttributes oAuthAttributes){
        User user = userRepository.findByEmail(oAuthAttributes.getEmail()).orElse(oAuthAttributes.toEntity());

        return userRepository.save(user);
    }
}
