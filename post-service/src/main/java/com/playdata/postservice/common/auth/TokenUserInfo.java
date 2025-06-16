package com.playdata.postservice.common.auth;

import com.playdata.postservice.common.entity.Role;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String email;
    private Role role;

}
