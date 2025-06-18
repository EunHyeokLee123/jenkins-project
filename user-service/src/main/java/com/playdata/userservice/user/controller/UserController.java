package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.JwtTokenProvider;
import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.user.dto.*;
import com.playdata.userservice.user.dto.UserLoginDto;
import com.playdata.userservice.user.dto.UserPasswordUpdateDto;
import com.playdata.userservice.user.dto.UserResDto;
import com.playdata.userservice.user.dto.UserSaveDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
         this.userService = userService;
         this.jwtTokenProvider = jwtTokenProvider;
     }
     @PostMapping("/create")
     public ResponseEntity<CommonResDto>   userSignIn( @RequestBody UserSaveDto userSaveDto) {

         User save = userService.Save(userSaveDto);
         CommonResDto resDto
                 = new CommonResDto(CREATED,
                 "User Created", save.getUsername());

         return new ResponseEntity<>(resDto, CREATED);
     }
    @PostMapping("/login")
    public ResponseEntity<CommonResDto> Login(@RequestBody  UserLoginDto  userLoginDto) {
            User user = userService.Login(userLoginDto);
        String token
                = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        Map<String ,Object> loginInfo = new HashMap<>();
        loginInfo.put("token",token);
        loginInfo.put("id",user.getId());
        loginInfo.put("role", user.getRole().toString());

       CommonResDto resDto = new CommonResDto(OK,"Login Success",loginInfo);
        return new ResponseEntity<>(resDto, OK);

    }
/*   @DeleteMapping("/{id}")   //회원 탈퇴
    public void deleteUser(@PathVariable Long id) {
         userService.deleteUser(id);
   }*/

    @PostMapping("/password")
    public ResponseEntity<?> saveUser(@RequestBody UserPasswordUpdateDto updateDto) {
        User user = userService.updatePassword(updateDto);

        if(user == null) {
            return new ResponseEntity<>(FORBIDDEN);
        }

        return new ResponseEntity<CommonResDto>(ACCEPTED);
    }
    
    // 다른 서비스에서 feign 클라이언트로 요청을 보내는 로직임
    // 절대 삭제하지 말것!
    // 본인이 사용하지 않는 메소드라고 해서 생각없이 삭제하는 몰상식한 행동을 하지 않길 바람.
    // 주석을 수정해봅니다!
    @GetMapping("/findByEmail")
    public CommonResDto findByEmail(@RequestParam String email) {
        User foundUser = userService.findUserIdByEmail(email);

        UserResDto build = UserResDto.builder()
                .email(foundUser.getEmail())
                .id(foundUser.getId())
                .name(foundUser.getUsername())
                .role(foundUser.getRole())
                .build();

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "유저 찾음", build);
        return resDto;
    }
    @GetMapping("/myinfo")
    public ResponseEntity<CommonResDto> getUser() {
        User user = userService.usersearch();

        UserInfoResponseDto responseDto = UserInfoResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .userId(user.getId())
                .build();

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "사용자 정보 조회 성공", responseDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    // 마이페이지에서 강사로 전환하는 버튼을 담당하는 메소드
    @GetMapping("/change-role")
    public ResponseEntity<?> changeRole(@AuthenticationPrincipal TokenUserInfo userInfo){
        UserResDto resDto = userService.changeRole(userInfo);
        if(resDto == null) {
            return new ResponseEntity<>(CONFLICT);
        }
        else{
            CommonResDto commonResDto = new CommonResDto(OK, "강사로의 전환이 완료됨", resDto);
            return new ResponseEntity<>(commonResDto, OK);
        }
    }


    // 카카오 콜백 요청 처리
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code , HttpServletResponse response) throws IOException {
        log.info("카카오 콜백 처리 시작! code: {}", code);

        String kakaoAccessToken = userService.getKakaoAccessToken(code);

        KakaoUserDto kakaoUserDto =userService.getKakaoUser(kakaoAccessToken);
        UserResDto userResDto = userService.findOrCreateKakaoUser(kakaoUserDto);
        String token = jwtTokenProvider.createToken(userResDto.getEmail(),userResDto.getRole().toString());

        String html = String.format("""
                <!DOCTYPE html>
                <html>
                <head><title>카카오 로그인 완료</title></head>
                <body>
                    <script>
                        if (window.opener) {
                            window.opener.postMessage({
                                type: 'OAUTH_SUCCESS',
                                token: '%s',
                                id: '%s',
                                role: '%s',
                                provider: 'KAKAO'
                            }, 'https://infolearnplaydata123456.shop');
                            window.close();
                        } else {
                            window.location.href = 'https://infolearnplaydata123456.shop';
                        }
                    </script>
                    <p>카카오 로그인 처리 중...</p>
                </body>
                </html>
                """, token, userResDto.getId(), userResDto.getRole().toString());
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(html);
    }

    @PostMapping("/email-valid")
    public  ResponseEntity <CommonResDto> emailValidate(@RequestBody Map<String,String> map    ) {
        String email = map.get("email");
        String authNum = userService.mailCheck(email);

        return ResponseEntity.ok().body(new CommonResDto(OK,"인증 요청 이메일 발송" ,authNum));
    }

    // 인증 코드를 검증하는 로직
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> map){
        log.info("인증 코드 검증! map: {}", map);

        Map<String, String> result = userService.verifyEmail(map);

        return ResponseEntity.ok().body("Success");
    }

    // 1단계: 인증 요청
    @GetMapping("/reset-password")
    public ResponseEntity<CommonResDto> requestResetPassword(@RequestParam String email) {

        userService.sendResetCode(email);
        return ResponseEntity.ok(new CommonResDto(OK, "인증 코드가 이메일로 전송되었습니다", true));
    }

    // 2단계: 인증 코드 검증
    @GetMapping("/verify-code")
    public ResponseEntity<CommonResDto> verifyResetCode(@RequestParam Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");
        boolean valid = userService.verifyResetCode(email, code);
        if (!valid) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new CommonResDto(BAD_REQUEST, "인증 코드가 일치하지 않습니다", true));
        }
        return ResponseEntity.ok(new CommonResDto(OK, "인증 성공", true));
    }

    // 3단계: 비밀번호 변경
    @PostMapping("/update-password")
    public ResponseEntity<CommonResDto> updatePassword(@RequestBody UserPasswordUpdateDto updateDto) {
        userService.updatePasswordAfterVerification(updateDto);
        return ResponseEntity.ok(new CommonResDto(OK, "비밀번호가 성공적으로 변경되었습니다", true));
    }
}
