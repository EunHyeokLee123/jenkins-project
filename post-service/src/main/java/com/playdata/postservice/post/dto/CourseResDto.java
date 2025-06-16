package com.playdata.postservice.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResDto {

    private Long productId;
    private String productName;
    private String description;
    private int price;
    private Long userId;
    private String category;
    private boolean active;

}
