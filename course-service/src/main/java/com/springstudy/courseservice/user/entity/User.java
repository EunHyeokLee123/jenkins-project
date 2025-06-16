package com.springstudy.courseservice.user.entity;


import com.springstudy.courseservice.user.dto.UserInfoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;


    private String password;

    private String email;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }



    public UserInfoDto fromEntity() {

        return UserInfoDto.builder()
                         .username(username)
                        .email(email)
                      .role(role)
                 .build();


    }
    //이렇게 구현한 이유는 세터는 필요할떄 꺼내는쓰는게 좋다

}
