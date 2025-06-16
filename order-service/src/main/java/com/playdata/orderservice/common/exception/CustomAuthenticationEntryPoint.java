package com.playdata.orderservice.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        System.out.println("커스텀 예외 핸들링 클래스의 메소드 호출");
        System.out.println(authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // ;charset=utf-8 -> 영어, 숫자 제외한 문자를 사용하려면 뒤에 붙여야
        // json 객체에 문자를 넣을 수 있음.
        response.setContentType("application/json;charset=utf-8");

        // Map 생성 및 데이터 추가 (JSON을 간편하게 만들 수 있음.)
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "NO_LOGIN");
        responseMap.put("code", "401");

        // Map을 JSON 문자열로 변환
        String jsonString = new ObjectMapper().writeValueAsString(responseMap);

        // JSON 문지열을 응답 객체에 실어서 클라이언트에게 응답 보냄
        response.getWriter().write(jsonString);


    }
}
