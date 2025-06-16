package com.playdata.userservice.user.dto;


import com.playdata.userservice.user.entity.Role;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class UserInfoResponseDto {
    private Long userId;
    private String username;
    private String email;
    private Role role;
}
