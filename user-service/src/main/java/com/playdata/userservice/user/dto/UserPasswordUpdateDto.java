package com.playdata.userservice.user.dto;


import com.playdata.userservice.user.entity.Role;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateDto {

    private String email;
    private String newPassword;
}
