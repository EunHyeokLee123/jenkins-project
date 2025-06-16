package com.springstudy.courseservice.user.controller;

import com.springstudy.courseservice.common.auth.JwtTokenProvider;
import com.springstudy.courseservice.common.dto.CommonResDto;
import com.springstudy.courseservice.user.dto.UserLoginDto;
import com.springstudy.courseservice.user.dto.UserPasswordUpdateDto;
import com.springstudy.courseservice.user.dto.UserResDto;
import com.springstudy.courseservice.user.dto.UserSaveDto;
import com.springstudy.courseservice.user.entity.User;
import com.springstudy.courseservice.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

    @PutMapping("/password")
    public ResponseEntity<CommonResDto> saveUser(@RequestBody UserPasswordUpdateDto updateDto) {
        User user = userService.updatePassword(updateDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "Password", user.getUsername());
        return new ResponseEntity<CommonResDto>(OK);
    }

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

      /*@GetMapping("/userInfo")
      public ResponseEntity<UserInfoResponseDto> userInfo(@RequestParam UserInfoDto userInfoDto){

             UserInfoResponseDto userInfoResponseDto = userService.userInfo(userInfoDto);
          return ResponseEntity.ok(userInfoResponseDto);
      }*/

}
