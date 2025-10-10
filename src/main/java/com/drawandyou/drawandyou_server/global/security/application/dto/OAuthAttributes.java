package com.drawandyou.drawandyou_server.global.security.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
// spring security 의 OAuth2 인증 과정에서
// 사용자 정보는 map<string,object> 형태로 제공됨
// 이 클래스를 통해 해당 데이터를 정형화된 형태로 다루기 쉽게 만드는 것
// 소셜 로그인 서비스로부터 전달받은 사용자 정보를, 우리 애플리케이션이 사용할 수있도록 담아주고 가공한느 역할
// OAuth2UserService 에서 사용
// 사용자가 소셜 로그인 완료 => oauth2 프로바이더인 구글이나 카카오로부터
// 사용자 정보를 전달받는데, 이 정보가 살짝식 다르기 때문에 하나의 통된 형태로 매핑을 해줘야한다!
// 그 작업을 이 클래스가 담당하는 것
// 예) 구글은 email 을 email , 네이버는 response.email, 카카오는 kakao_account.email 이런식으로 보내니까..
public class OAuthAttributes {

    private Map<String, Object> attributes; // OAuth2 제공자에서 가져온 모든 사용자 속
    // 이름, 이메일 ,프로필 사진 등 다양한 정보... 가 담겨있음
    private String nameAttributeKey; // 사용자 식별에 사용할 키 이름 (구글은 sub, 네이버는 id ...)
    private String name; // 사용자 이름
    private String email; // 사용자 이메일
    private String picture; // 프로필 사진 URL
    private String id; // OAuth 사용자 고유 id (실제 유저 식별 값)

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String id) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.id = id;
    }

    // oauth 제공자 구분에 따라 처리할 메서드 (현재는 google 만 지원)
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    // google oauth 사용자 정보로부터 oauthattributes 객첼르 생성하는 메서드
    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes ) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name")) // 이름
                .email((String) attributes.get("email")) //
                .picture((String) attributes.get("picture"))
                .id((String) attributes.get(userNameAttributeName)) // 사용자 id
                .attributes(attributes) // 전체 속성 map
                .nameAttributeKey(userNameAttributeName) // 사용자 식별 키
                .build();
    }








}
