package com.drawandyou.drawandyou_server.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.drawandyou.drawandyou_server.global.security.RedirectUrlCookieFilter.REDIRECT_URL_PARAM;

@Slf4j
@AllArgsConstructor
@Component
// 소셜 로그인이 성공한후에는, 단순히 성공했다는 응답만 주는것이 아니라
// 사용자 정보를 기반으로 jwt 토큰을 주거나, 특정 페이지로 리다이렉트 시키거나 하는 후속 처리 로직이 필요하기 때문.
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // oauth2 로그인 성공시 처리 로직을 담당하는 클래스

    // 이 url 은, 소셜 로그인 성공후 사용자를 리다이렉트 시킬때 , 쿠키나 리디렉션 정보가 없을 경우 사용하는 기본 url
    // 최후에 보낼 url 임 .
    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000"; // 기본 리다이렉트 주소

    // 로그인 인증 성공시 호출되는 메소드

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        TokenProvider tokenProvider = new TokenProvider(); // jwt 토큰 발급을 위한 객체 생성
        String token = tokenProvider.create(authentication); // 인증 정보 기반으로 jwt 토큰 생성

        // 요청에 포함된 쿠키 중 redirect_url 이름의 쿠키를 찾아 Optional 로 래핑
        Optional<Cookie> oCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REDIRECT_URL_PARAM))
                .findFirst();

        // 쿠키가 존재하면 값 추출, 없으면 optional.empty
        Optional<String> redirectUrl = oCookie.map(Cookie::getValue);

        // 쿠키값 존재하면 해당 값 사용. 없으면 기본 주소 사용후
        // 이후 jwt 토큰을 쿼리 파라미터로 추가
        String targetUrl = redirectUrl.orElseGet(() -> LOCAL_REDIRECT_URL) + "/sociallogin?token=" + token;

        response.sendRedirect(targetUrl); // 사용자를 최종 url 로 리다이렉트

    }
}
