package com.playdata.userservice.user.service;

import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.user.dto.*;
import com.playdata.userservice.user.entity.Role;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;


import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

      private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final MailSenderService mailSenderService;

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis key 상수
    private static final String VERIFICATION_CODE_KEY = "email_verify:code:";
    private static final String VERIFICATION_ATTEMPT_KEY = "email_verify:attempt:";
    private static final String VERIFICATION_BLOCK_KEY = "email_verify:block:";

    @Value("${oauth2.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Transactional
    public User Save(UserSaveDto userSaveDto) {
        String email = userSaveDto.getEmail();
        String username = userSaveDto.getUsername();
        String password = userSaveDto.getPassword();

        // 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user  = User.builder()
                        .username(username)
                       .email(email)
                     .password(encodedPassword)
                             .role(userSaveDto.getRole())
                .build();

        return userRepository.save(user);
    }

   @Transactional
   public User updatePassword (UserPasswordUpdateDto updateDto) {
         //비밀번호변경
       User user  = userRepository.findByemail(updateDto.getEmail())
               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + updateDto.getEmail()));

       if(passwordEncoder.matches(updateDto.getNewPassword(), user.getPassword())) {
           return null;
       }
       String encodedPassword = passwordEncoder.encode(updateDto.getNewPassword());

       user.changePassword(encodedPassword);
         return user;

   }

   @Transactional
    public void deleteUser (Long id) {   //회원탈퇴
       userRepository.deleteById(id);
   }

    @Transactional
    public User Login (UserLoginDto userLoginDto) {   //로그인
        User user = userRepository.findByemail(userLoginDto.getEmail() ).orElseThrow(() -> new EntityNotFoundException("User not found!"));

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 틀렸습니다.");
        }

        return user;

     }

    @Transactional(readOnly = true)
    public User usersearch() {
        TokenUserInfo userInfo
                // 필터에서 세팅한 시큐리티 인증 정보를 불러오는 메서드
                = (TokenUserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userRepository.findByemail(userInfo.getEmail())
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found!")
                );



        return  User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .id(user.getId())
                .build();

    }



    @Transactional(readOnly = true)
    public User findUserIdByEmail(String email) {

        User foundUser = userRepository.findByemail(email).orElseThrow(() -> new EntityNotFoundException("User not found!"));

        return foundUser;
    }

    public String getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("client_id", kakaoClientId);
        formData.add("redirect_uri", kakaoRedirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, Map.class);

        Map<String, Object> responseJSON = (Map<String, Object>) responseEntity.getBody();
        return (String) responseJSON.get("access_token");
    }

    public KakaoUserDto getKakaoUser(String kakaoAccessToken) {
        String requestUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + kakaoAccessToken);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoUserDto> response = restTemplate.exchange(
                requestUrl, HttpMethod.GET, new HttpEntity<>(headers), KakaoUserDto.class
        );

        return response.getBody();
    }
    @Transactional
    public UserResDto findOrCreateKakaoUser(KakaoUserDto kakaoUserDto) {
        Optional<User> existingUser = userRepository.findBySocialProviderAndSocialId("KAKAO", kakaoUserDto.getId().toString());

        if (existingUser.isPresent()) {
            return existingUser.get().toDto();
        } else {
            User newUser = User.builder()
                    .username(kakaoUserDto.getProperties().getNickname())
                    .email(kakaoUserDto.getKakaoAccount().getEmail())
                    .profileImage(kakaoUserDto.getProperties().getProfileImage())
                    .socialId(kakaoUserDto.getId().toString())
                    .socialProvider("KAKAO")
                    .password(null)
                    .build();

            return userRepository.save(newUser).toDto();
        }
    }

    public String mailCheck(String email) {
        // 차단 상태 확인
        if(isBlocked(email)){
            throw new IllegalArgumentException("blocked email");
        }
        Optional<User> foundEmail =
                userRepository.findByemail(email);
        // 이미 존재하는 이메일인 경우 -> 회원가입 불가
        if (foundEmail.isPresent()) {
            // 이미 존재하는 이메일이라는 에러를 발생 -> controller가 이 에러를 처리
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String authNum;
        // 이메일 전송만을 담당하는 객체를 이용해서 이메일 로직 작성.
        try {
            authNum = mailSenderService.joinMain(email);
        } catch (MessagingException e) {
            log.info(e.getMessage());
            throw new RuntimeException("이메일 전송 과정 중 문제 발생");
        }

        // 인증 코드를 redis에 저장하자
        String key = VERIFICATION_CODE_KEY + email;
        redisTemplate.opsForValue().set(key, authNum, Duration.ofMinutes(2));

        return authNum;
    }

    public Map<String, String> verifyEmail(Map<String, String> map) {

        String email = map.get("email");
        String code = map.get("code");

        // 차단 상태 확인
        // 차단된 이메일인 경우
        if(isBlocked(email)) {
            throw new IllegalArgumentException("blocked email: " + email);
        }

        // redis에 저장된 인증 코드 조회
        String key = VERIFICATION_CODE_KEY + email;
        Object foundCode = redisTemplate.opsForValue().get(key);
        // 인증 코드 유효시간이 만료된 경우
        if(foundCode == null) {
            throw new IllegalArgumentException("Authcode expired.");
        }

        // 인증 시도 횟수 증가
        int attemptCount = incrementAttemptCount(email);

        // 조회한 코드와 사용자가 입력한 코드가 일치한 지 검증
        if(!foundCode.toString().equals(code)) {
            // 인증 코드를 틀린 경우
            if(attemptCount >= 3){
                // 최대 시도 횟수 초과 시 해당 이메일 인증 차단
                blockUser(email);
                throw new IllegalArgumentException("email blocked.");
            }
            int remainingAttempt = 3 - attemptCount;
            throw new IllegalArgumentException(String.format("authCode wrong!, %d", remainingAttempt));
        }

        log.info("이메일 인증 성공!, email: {}", email);

        // 인증 완료 했기 때문에, redis에 있는 인증 관련 데이터를 삭제하자.
        redisTemplate.delete(key);

        return map;
    }

    private boolean isBlocked(String email) {
        String key = VERIFICATION_BLOCK_KEY + email;
        return redisTemplate.hasKey(key);
    }

    private void blockUser(String email) {

        String key = VERIFICATION_BLOCK_KEY + email;
        redisTemplate.opsForValue().set(key, "blocked", Duration.ofMinutes(30));

    }

    private int incrementAttemptCount(String email) {

        String key = VERIFICATION_ATTEMPT_KEY + email;
        Object obj = redisTemplate.opsForValue().get(key);

        int count = (obj != null) ? Integer.parseInt(obj.toString()) + 1 : 1;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofMinutes(1));

        return count;
    }

    @Transactional
    public void sendResetCode(String email) {
        User user = userRepository.findByemail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));
        String code = String.valueOf((int) ((Math.random() * 900000) + 100000)); // 6자리 숫자
        redisTemplate.opsForValue().set("reset:" + email, code, Duration.ofMinutes(5));
        try {
            mailSenderService.sendAuthCode(email, code);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패");
        }
    }

    public boolean verifyResetCode(String email, String inputCode) {
        String redisKey = "reset:" + email;
        String savedCode = redisTemplate.opsForValue().get(redisKey).toString();
        return savedCode != null && savedCode.equals(inputCode);
    }

    @Transactional
    public void updatePasswordAfterVerification(UserPasswordUpdateDto updateDto) {
        String email = updateDto.getEmail();
        User user = userRepository.findByemail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(updateDto.getNewPassword());
        user.changePassword(encodedPassword);
        userRepository.save(user);
        redisTemplate.delete("reset:" + email); // 인증 코드 삭제
    }

    // 강사로 Role을 변환하는 메소드
    public UserResDto changeRole(TokenUserInfo userInfo) {

        User foundUser
                = userRepository.findByemail(userInfo.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found! " + userInfo.getEmail()));

        if(foundUser.getRole() == Role.USER) {
            foundUser.changeRole(Role.ADMIN);
            return userRepository.save(foundUser).toDto();
        }
        else{
            return null;
        }
    }
}
