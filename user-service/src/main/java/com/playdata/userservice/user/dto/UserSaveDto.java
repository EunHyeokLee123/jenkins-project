package com.playdata.userservice.user.dto;


import com.playdata.userservice.user.entity.Role;
import com.playdata.userservice.user.entity.User;
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


   @Builder.Default
   private Role role =Role.USER;



}
