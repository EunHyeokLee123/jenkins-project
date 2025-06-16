package com.playdata.postservice.common.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorDto {

    private int statusCode;
    private String statusMessage;

    public CommonErrorDto(HttpStatus httpStatus, String statusMessage) {
        this.statusCode = httpStatus.value();
        this.statusMessage = statusMessage;
    }



}
