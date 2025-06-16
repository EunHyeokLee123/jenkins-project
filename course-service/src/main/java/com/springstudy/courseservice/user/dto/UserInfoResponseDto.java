package com.springstudy.courseservice.user.dto;


import com.springstudy.courseservice.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserInfoResponseDto {
    private String username;
    private String email;
    private Role role;
}
