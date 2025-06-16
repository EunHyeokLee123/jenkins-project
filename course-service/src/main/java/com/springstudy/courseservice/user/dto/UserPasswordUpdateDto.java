package com.springstudy.courseservice.user.dto;


import com.springstudy.courseservice.user.entity.Role;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateDto {

    private Long Id;
    private String email;
    private String Password;
    private Role Role;
}
