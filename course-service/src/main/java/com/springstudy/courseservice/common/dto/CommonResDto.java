package com.springstudy.courseservice.common.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter @Setter @ToString
@NoArgsConstructor
@Data
public class CommonResDto<T> {

    private int statusCode;
    private String statusMessage;
    private T result;

    public CommonResDto(HttpStatus httpStatus, String statusMessage, T result) {
        this.statusCode = httpStatus.value();
        this.statusMessage = statusMessage;
        this.result = result;
    }

}
