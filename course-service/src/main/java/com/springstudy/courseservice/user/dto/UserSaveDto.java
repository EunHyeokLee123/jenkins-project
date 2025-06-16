package com.springstudy.courseservice.user.dto;


import com.springstudy.courseservice.user.entity.Role;
import com.springstudy.courseservice.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSaveDto {

     @NotNull(message = "공백안됩니다 ")
   private String username;

   @NotNull(message = "공백안됩니다")
   private String password;
   @NotNull(message = "공백안됩니다 ")
   private String email;

   private Role role;


   public User toEntity() {
      return  User.builder()
              .email(this.email)
              .password(this.password)
              .username(this.username)
              .role(Role.valueOf(this.role.name()))
              .build();
   }

}
