package com.playdata.postservice.post.dto;


import com.playdata.postservice.post.entity.Post;
import com.playdata.postservice.post.entity.PostStatus;
import lombok.Getter;

@Getter
public class PostSaveReqDto {

    private Long productId;
    private String title;
    private String content;

    public Post toEntity(Long userId){
        return Post.builder()
                .userId(userId)
                .productId(productId)
                .title(title)
                .content(content)
                .status(PostStatus.UNANSWERED)
                .build();
    }

}
