package com.springstudy.courseservice.user.dto;

import com.springstudy.courseservice.user.entity.Role;
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

}