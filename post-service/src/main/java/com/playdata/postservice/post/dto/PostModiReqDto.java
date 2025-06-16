package com.playdata.postservice.post.dto;

import lombok.Getter;

@Getter
public class PostModiReqDto {

    private Long postId;
    private String title;
    private String content;

}
