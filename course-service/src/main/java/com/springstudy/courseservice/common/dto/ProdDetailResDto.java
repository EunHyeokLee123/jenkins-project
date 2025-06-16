package com.springstudy.courseservice.common.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProdDetailResDto {
    private Long productId;
    private String productName;
    private String prodDesc;
    private String prodPrice;
    private String category;
    private String filePath;
    //active, userid


}
