package com.springstudy.courseservice.user.service;

import com.springstudy.courseservice.common.auth.TokenUserInfo;
import com.springstudy.courseservice.user.entity.User;
import com.springstudy.courseservice.user.repository.UserRepository;
import com.springstudy.courseservice.user.dto.UserInfoDto;
import com.springstudy.courseservice.user.dto.UserLoginDto;
import com.springstudy.courseservice.user.dto.UserPasswordUpdateDto;
import com.springstudy.courseservice.user.dto.UserSaveDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

      private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


     @Transactional
    public User Save(UserSaveDto userSaveDto) {
         String email = userSaveDto.getEmail();//회원가입
        String username = userSaveDto.getUsername();
         String password = userSaveDto.getPassword();

         String encodedPassword = passwordEncoder.encode(password);
         // 이메일 중복 체크
         if (userRepository.existsByEmail(email)) {
             throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
         }


        userSaveDto.setPassword(encodedPassword);
        userSaveDto.setUsername(username);
        userSaveDto.setEmail(email);

        User save = userRepository.save(userSaveDto.toEntity());


         return save;
    }



   @Transactional
   public User updatePassword (UserPasswordUpdateDto updateDto) {
         //비밀번호변경
       User user  = userRepository.findById(updateDto.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found: " + updateDto.getId()));
       String encodedPassword = passwordEncoder.encode(updateDto.getPassword());
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
      @Transactional
       public UserInfoDto myInfo () {
           TokenUserInfo userInfo
                   = (TokenUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


           User user = userRepository.findByemail(userInfo.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found!"));

        return user.fromEntity();
       }




    public User findUserIdByEmail(String email) {

        User foundUser = userRepository.findByemail(email).orElseThrow(() -> new EntityNotFoundException("User not found!"));

        return foundUser;
    }
}
