package com.playdata.evalservice.eval.dto;

import com.playdata.evalservice.common.entity.Role;
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