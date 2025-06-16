package com.playdata.postservice.post.dto;


import com.playdata.postservice.post.entity.PostStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostResDto {

    private Long id;

    private Long userId;

    private Long productId;

    private String title;

    private String content;

    private PostStatus status;


}
