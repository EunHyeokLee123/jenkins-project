package com.playdata.userservice.user.dto;


import com.playdata.userservice.user.entity.Role;
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
