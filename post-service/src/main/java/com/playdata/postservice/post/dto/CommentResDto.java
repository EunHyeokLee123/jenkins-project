package com.playdata.postservice.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Builder
public class CommentResDto {

    private Long commentId;
    private Long userId;
    private String content;
    private Long postId;

}
