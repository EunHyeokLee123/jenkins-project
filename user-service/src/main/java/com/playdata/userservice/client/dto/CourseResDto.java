package com.playdata.userservice.client.dto;

import com.playdata.userservice.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResDto {
    private Long productId;
    private String productName;
    private String description;
    private int price;

    private String category;
    private boolean active;
    private String filePath;


}
