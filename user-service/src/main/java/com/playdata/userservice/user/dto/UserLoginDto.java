package com.playdata.userservice.user.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

private   String  Email;
private   String password;


}
