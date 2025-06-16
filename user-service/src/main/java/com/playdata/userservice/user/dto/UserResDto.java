package com.playdata.userservice.user.dto;

import com.playdata.userservice.user.entity.Role;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResDto {

    private String email;
    private Long id;
    private String name;

    private Role role;
    private String profileImage;
    private String socialProvider;

}