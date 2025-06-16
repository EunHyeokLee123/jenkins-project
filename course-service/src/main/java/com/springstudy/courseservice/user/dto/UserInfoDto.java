package com.springstudy.courseservice.user.dto;


import com.springstudy.courseservice.user.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {

    private String username;
    private String email;
    private Role role;


}
