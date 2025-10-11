package com.drawandyou.drawandyou_server.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
// 스프링 시큐리티에서 소셜 로그인을 처리할때
// 클라이언트가 요청한 redirect url 을 쿠키에 저장하는 용도로 사용된다.
// redirect_url 파라미터를 쿠키로 저장해놓고, 로그인 완료 후 해당 url 로 리다이렉트 하기 위함
// 소셜 로그인 요청이 들어올때마다 한 번씩 실행되어, 리다이렉션 정보를 쿠키에 저장해주는 필터
public class RedirectUrlCookieFilter extends OncePerRequestFilter {

    public static final String REDIRECT_URL_PARAM = "redirect_url"; // 요청 파라미터 및 쿠키 이름

    private static final int MAX_AGE = 180; // 쿠키 유효 시간 : 180초(로그인 시 임시로 저장됙에, 너무 오래있을 필요 없음)

    // 필터 로직 구현 메소드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // /oauth2/authorization 으로 시작하는 요청이 들어오면 처리
        if (request.getRequestURI().startsWith("/oauth2/authorization")) {
            try{
                String redirectUrl = request.getParameter(REDIRECT_URL_PARAM); // redirect_url 의 파라미터 가져오기

                // 쿠키 생성 및 설정
                Cookie cookie = new Cookie(REDIRECT_URL_PARAM, redirectUrl);
                cookie.setPath("/"); // 전체 경로에서 쿠키 사용 가능하도록
                cookie.setHttpOnly(true); // 자바 스크립트에서 접근 불가능하도록 설정
                cookie.setMaxAge(MAX_AGE);

                response.addCookie(cookie);
            }catch (Exception e){
                log.error("could not set user authentication in security context", e);
            }
        }

        // 다음 필터 체인으로 요청 전달
        filterChain.doFilter(request,response);

    }
}
