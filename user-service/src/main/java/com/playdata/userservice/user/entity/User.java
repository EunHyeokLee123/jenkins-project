package com.playdata.userservice.user.entity;


import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.user.dto.UserInfoDto;
import com.playdata.userservice.user.dto.UserResDto;
import com.playdata.userservice.user.dto.UserSaveDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "user_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;


    private String password;

    private String email;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Column
    private String socialId; // 소셜 로그인 고유 ID

    @Column
    private String profileImage; // 프로필 이미지 url

    @Column
    private String socialProvider; // GOOGLE, KAKAO, NAVER, null(일반 로그인)

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }


    //이렇게 구현한 이유는 세터는 필요할떄 꺼내는쓰는게 좋다
      public UserSaveDto toEntity() {
          return UserSaveDto.builder()
                          .username(this.username)
                                  .email(this.email)
                  .password(this.password)
                  .role(this.role)
                  .build();
      }

    public UserResDto toDto() {
        return  UserResDto.builder()

                .email(this.email)
                .name(this.username)
                .profileImage(this.profileImage)
                .socialProvider(this.socialProvider)
                .role(this.role)


                .build();
    }

}
