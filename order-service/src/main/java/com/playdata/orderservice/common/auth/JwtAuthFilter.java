package com.playdata.orderservice.common.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 클라이언트가 전송한 토큰을 검사하는 필터
// spring security에 등록해서 사용할 것임.
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

//    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 이제는 gateway가 토큰 내의 클레임을 헤더에 담아서 보내줌.
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");
        log.info("userEmail: {}, userRole: {}", userEmail, userRole);

        // token에 null이 들어갈 수 있으니 null 체크

        if (userEmail != null && userRole != null) {

                // spring security에게 전달할 인가 정보 리스트를 생성. (권한 정보)
                // 권한이 여러 개 존재할 경우 리스트로 권한 체크에 사용할 필드를 add.
                // (권한 여러개면 여러번 add 가능)
                // 나중에 컨트롤러의 요청 메서드마다 권한을 파악하게 하기 위해 미리 저장을 해 놓는 것.
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
                // _는 security의 입력 규칙임. ex) ROLE_USER, ROLE_ADMIN
                // 나중에 ROLE을 꺼내올 때 security가 ROLE_를 붙여서 검색을 함.
                authorityList.add(new SimpleGrantedAuthority("ROLE_"
                        + userRole));
                // 인터페이스 형태로 짧게 선언
                // 인증 완료 처리
                // 위에서 준비한 여러가지 사용자 정보, 인가정보 리스트를 하나의 객체로 포장
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        new TokenUserInfo(userEmail, Role.valueOf(userRole)), // controller 등에서 활용할 USER 정보
                        "",       // 인증된 사용자의 비밀번호: 보통 null 혹은 빈 문자열로 선언
                        authorityList  // 인가 정보 (권한 확인용)
                );

                // Security 컨테이너에 인증 정보 객체를 등록
                // 인증 정보를 전역적으로 어느 컨테이너, 혹은 서비스에서나 활용할 수 있도록 미리 저장
                SecurityContextHolder.getContext().setAuthentication(auth);


            }

            // Filter를 통과하는 메소드 (doFilter를 호출하지 않으면 필터 통과가 안됨.)
            // 토큰의 소유 여부와 관계없이 필터를 통과해야 하긴 함.
            filterChain.doFilter(request, response);


    }

}
