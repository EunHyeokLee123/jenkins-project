package com.playdata.postservice.post.dto;

import com.playdata.postservice.post.entity.Post;
import com.playdata.postservice.post.entity.PostStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostCoLengthDto {

    private Long id;

    private Long userId;

    private Long productId;

    private String title;

    private String content;

    private PostStatus status;

    private int commentCount;

    public PostCoLengthDto onlyAddLength(Post post, int commentCount) {
        return PostCoLengthDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .productId(post.getProductId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .commentCount(commentCount)
                .build();
    }

}
