package com.drawandyou.drawandyou_server.global.security.vo;

import com.drawandyou.drawandyou_server.global.security.application.dto.OAuthAttributes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;

public class CustomUser extends DefaultOAuth2User {

    private static final long serialVersionUID = 1L; // 직렬화 버전 UID


    private Long id; // 사용자 고유 id
    private String email; // 사용자 고유 이메일
    private String username; // 사용자 이름 또는 닉네임

    // 왜 별도 필도로 관리할까?
    // defaultoauth2user 에서는 , 이 정보들을 map<String,object> 로 가지고 있기 때문에..
    // 일일이 꺼내 써야한다..

    // 즉, 우리가 소셜 로그인으로 받아온 사용자 정보 중에서 자주 사용하는 값만 따로 필드로 꺼내 관리하기 위한 클래스임!

    // oauth2 인증 정보를 기반으로 customuser 객체 생성
    public CustomUser(
            Long id,
            String email,
            String username,
            Collection<? extends GrantedAuthority> authorities, // 권한 목록
            OAuthAttributes attributes // oauth 사용자 정보 dto
    ){
        // spring security 에서 사용자 정보 처리할때
        // 권한 정보 , 속성 map, 식별 키를 필요로 하기 때문에 부모 생성자로 전달.
        super(authorities, attributes.getAttributes(), attributes.getNameAttributeKey());

        // 추가 필드 초기화.
        this.id = id;
        this.email = email;
        this.username = username;
    }

    // oauth2user 인터페이스 구현 : 사용자의 고유 이름(id) 를 문자열로 반환
    // 반드시 구현해야 하는 필수 메소드임
    // 시큐리티는 이 사용자느 누구인가? 를 식별할때 getName 을 호출하여 고유한 사용자 식별값을 가져간다.
    @Override
    public String getName() {
        return "" + this.id; // id 를 문자열로 반환하여 반환한다. (우리만의 기준으로 정한거임)
    }
}
