package com.playdata.postservice.post.dto;

import com.playdata.postservice.common.entity.Role;
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