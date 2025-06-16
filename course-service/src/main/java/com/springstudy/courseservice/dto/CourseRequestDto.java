package com.springstudy.courseservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequestDto {
    private String productName;
    private String description;
    private int price;
    private String category;
    private String filePath;
}