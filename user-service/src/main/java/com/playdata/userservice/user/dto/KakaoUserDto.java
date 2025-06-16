package com.playdata.userservice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoUserDto {

    private Long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    private Properties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    @ToString
    public static class Properties {
        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Getter
    @Setter
    @ToString
    public static class KakaoAccount {
        private String email;
    }
}