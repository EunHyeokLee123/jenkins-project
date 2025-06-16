package com.playdata.evalservice.common.auth;

import com.playdata.evalservice.common.entity.Role;
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
