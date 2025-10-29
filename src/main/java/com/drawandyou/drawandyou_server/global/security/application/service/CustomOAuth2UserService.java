package com.drawandyou.drawandyou_server.global.security.application.service;

import com.drawandyou.drawandyou_server.global.security.application.dto.OAuthAttributes;
import com.drawandyou.drawandyou_server.global.security.vo.CustomUser;
import com.drawandyou.drawandyou_server.user.domain.entity.User;
import com.drawandyou.drawandyou_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser");

        // 기본 oauth2 사용자 정보 제공 서비스 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // 사용자 정보 조회

        // oauth 공급자 이름 (google, naver, github 등...)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 사용자 식별을 위한 키이름 ( sub, id 등)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        log.info("loadUser registrationId = {}", registrationId);
        log.info("loadUser name = {}", userNameAttributeName);

        // 공급자로부터 받은 사용자 정보를 OAuthAttributes dTO 로 매핑
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 사용자 정보 추출
        String name = attributes.getName();
        String email = attributes.getEmail();
        // String picture = attributes.getPicture(); // 프로필 사진 - 현재 사용하지 않음
        // String id = attributes.getId(); // 고유 id - 현재 사용하지 않음
        String socialType = "google"; // 구글만 지원



        // null 방지 처리
        if (name == null) name = "";
        if (email == null) email = "";

        // 권한 목록 생성(기본 권한 ROLE_USER 부여) .
        // 소셜 로그인 사용자에게는 기본적으로 role_user 권한 부여
        // 필요하다면 더 다양한 권한을 부여할 수도 있음.
        List<SimpleGrantedAuthority> authories = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authories.add(authority);

        // username에는 실제 사용자 이름 저장, email은 별도 필드에 저장
        String username = name; // 구글에서 제공하는 실제 이름

        String authProvider = socialType; // oauth 제공자 정보

        User userEntity = null; // 사용자 정보 저장 객체

        // 이메일로 기존 사용자 확인 (이메일을 고유 식별자로 사용)
        if (!userRepository.existsByEmail(email)) {
            // 새로운 사용자 생성
            userEntity = User.builder()
                    .username(username)  // 실제 사용자 이름
                    .email(email)        // 이메일 주소
                    .authProvider(authProvider)
                    .build();
            userEntity = userRepository.save(userEntity);
        } else {
            userEntity = userRepository.findByEmail(email); // 기존 사용자 조회
        }


        // 사용자 정보 담은 CustomUser 객체로 반환
        return new CustomUser(userEntity.getId(), email, name, authories, attributes);
    }
}
