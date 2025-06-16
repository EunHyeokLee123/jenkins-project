package com.springstudy.courseservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDto {
    private Long productId;
    private String productName;
    private String description;
    private int price;
    private Long userId;
    private String category;
    private boolean active;
    private String filePath;
    private String username;

    @Override
    public String toString() {
        return "CourseResponse{" +
                "productId=" + productId +
                ", prodName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", filePath='" + filePath + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}