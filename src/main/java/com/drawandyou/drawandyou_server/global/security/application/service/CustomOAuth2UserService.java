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
        String nameAttributeKey = attributes.getNameAttributeKey(); //사용자 식별 키
        String name = attributes.getName();
        String email = attributes.getEmail();
        String picture = attributes.getPicture();
        String id = attributes.getId(); // 고유 id
        String socialType = "google"; //현재는 구글만 지원한다고 가정

        // 소셜 로그인 제공자에따라 socialtYPE 설정
//        if (registrationId.equals("naver")) {
//            socialType = "naver";
//        } else if (registrationId.equals("kakao")) {
//            socialType = "kakao";
//        } else if (registrationId.equals("github")) {
//            socialType = "github";
//
//            // 깃허브의 경우 이메일이 없을 수 있기 때문에 추가요청으로 가져온다
//            if (email == null) {
//                email = getEmailFromGitHub(userRequest.getAccessToken().getTokenValue());
//            }
//        } else{
//            socialType = "google";
//
//        }



        // null 방지 처리
        if (name == null) name = "";
        if (email == null) email = "";

        // 권한 목록 생성(기본 권한 ROLE_USER 부여) .
        // 소셜 로그인 사용자에게는 기본적으로 role_user 권한 부여
        // 필요하다면 더 다양한 권한을 부여할 수도 있음.
        List<SimpleGrantedAuthority> authories = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authories.add(authority);

        // 카카오에서 이메일이 없는 경우 대체 username 생성
        String username = email;
        if (username == null || username.isEmpty()) {
            username = socialType + "_" + id; // 예: kakao_123456789
        }

        String authProvider = socialType;// oauth 제공자 정보

        User userEntity = null; // 사용자 정보 저장 객체

        // 사용자 정보 없으면 새로 저장
        if (!userRepository.existsByUsername(username)) {
            userEntity = User.builder()
                    .username(username)
                    .authProvider(authProvider)
                    .build();
            userEntity = userRepository.save(userEntity);
        } else{
            userEntity = userRepository.findByUsername(username); // 기존 사용자 조회
        }


        // 사용자 정보 담은 CustomUser 객체로 반환
        return new CustomUser(userEntity.getId(), email, name, authories, attributes);
    }
}
