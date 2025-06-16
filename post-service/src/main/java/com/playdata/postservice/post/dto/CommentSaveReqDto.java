package com.playdata.postservice.post.dto;

import com.playdata.postservice.post.entity.Comment;
import lombok.Getter;

@Getter
public class CommentSaveReqDto {

    Long postId;
    String content;

}
