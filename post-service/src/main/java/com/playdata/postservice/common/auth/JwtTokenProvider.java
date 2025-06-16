package com.playdata.postservice.common.auth;

import com.playdata.postservice.common.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
// 역할: 토큰을 발급하고, 서명의 위조를 검사해주는 객체
public class JwtTokenProvider {

    // yml 파일에 있는 설정값 가져오기
    // 서명에 사용할 값 (512bit 이상의 랜덤 문자열을 권장)
    @Value("${jwt.secretKey}") // lombok아니고 springframework임.
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private int expiration;

    // Refresh token용 변수
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    @Value("${jwt.expirationRt}")
    private int expirationRt;

    // 토큰 생성 메서드
     /*
            {
                "iss": "서비스 이름(발급자)",
                "exp": "2023-12-27(만료일자)",
                "iat": "2023-11-27(발급일자)",
                "email": "로그인한 사람 이메일",
                "role": "Premium"
                ...
                == 서명
            }
     */

    public String createToken(String email, String role){
        // Claims: 페이로드에 들어갈 사용자 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                // 발행시간
                .setIssuedAt(now)
                // 만료시간
                // 현재 시간 밀리초에 30분을 더한 시간 만큼 만료시간으로 세팅
                // 밀리초로 리턴되기에 밀리초로 환산해서 계산해야 함.
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000))
                // 서명을 어떤 알고리즘으로 암호화 할 지
                .signWith(SignatureAlgorithm.HS512, secretKey)
                // 위의 값들을 String으로 compact화 해서 리턴.
                .compact();
    }

    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조 여부를 확인
     * 토큰을 json으로 파싱해서 클레임(토큰 정보)을 리턴
     *
     * @param token - 필터가 전달해 준 토큰
     * @return - 토큰 안에 있는 인증된 유저 정보를 반환
     */

    // token의 유효성 여부를 확인하는 메소드
    public TokenUserInfo validateAndGetTokenUserInfo(String token) throws Exception {
        Claims claims = Jwts.parserBuilder()
                // token 발급자의 발급 당시의 서명을 넣어줌.
                .setSigningKey(secretKey)
                // token 유효성 검사를 해주는 Parser 객체 생성
                // 생성 과정에서 서명이 위조된 경우에는 예외가 발생
                // 위조되지 않았다면(예외가 발생하지 않았다면),
                // payload를 리턴해줌.
                .build()
                // 리턴된 payload에서 Claim을 파싱함.
                .parseClaimsJws(token)
                // Claim을 리턴
                .getBody();

        System.out.println("claims = " + claims);

        return TokenUserInfo.builder()
                // email을 claim의 subject로 setting했기에
                .email(claims.getSubject())
                // Claim이 바로 Role(enum)으로 변환이 안되기에
                // String으로 꺼내고 valueOf를 통해 Role로 변환해줌
                .role(Role.valueOf(claims.get("role", String.class)))
                .build();

    }

    // Refresh Token 용 메소드

    public String createRefreshToken(String email, String role){
        // Claims: 페이로드에 들어갈 사용자 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();


        return Jwts.builder()
                .setClaims(claims)
                // 발행시간
                .setIssuedAt(now)
                // 토큰 생성 로직은 동일하나 서명과 만료기간이 다르게 세팅됨.
                .setExpiration(new Date(now.getTime() + expirationRt * 60 * 1000))
                // 서명을 어떤 알고리즘으로 암호화 할 지
                .signWith(SignatureAlgorithm.HS512, secretKeyRt)
                // 위의 값들을 String으로 compact화 해서 리턴.
                .compact();
    }

    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조 여부를 확인
     * 토큰을 json으로 파싱해서 클레임(토큰 정보)을 리턴
     *
     * @param token - 필터가 전달해 준 토큰
     * @return - 토큰 안에 있는 인증된 유저 정보를 반환
     */

    // token의 유효성 여부를 확인하는 메소드 -> 필요없네
    public TokenUserInfo validateAndGetRefreshTokenUserInfo(String token)
            throws Exception {
        Claims claims = Jwts.parserBuilder()
                // token 발급자의 발급 당시의 서명을 넣어줌.
                .setSigningKey(secretKeyRt)
                // token 유효성 검사를 해주는 Parser 객체 생성
                // 생성 과정에서 서명이 위조된 경우에는 예외가 발생
                // 위조되지 않았다면(예외가 발생하지 않았다면),
                // payload를 리턴해줌.
                .build()
                // 리턴된 payload에서 Claim을 파싱함.
                .parseClaimsJws(token)
                // Claim을 리턴
                .getBody();

        System.out.println("claims = " + claims);

        return TokenUserInfo.builder()
                // email을 claim의 subject로 setting했기에
                .email(claims.getSubject())
                // Claim이 바로 Role(enum)으로 변환이 안되기에
                // String으로 꺼내고 valueOf를 통해 Role로 변환해줌
                .role(Role.valueOf(claims.get("role", String.class)))
                .build();
    }
}

