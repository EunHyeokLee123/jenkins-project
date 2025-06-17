package com.playdata.postservice.common.exception;

import com.playdata.postservice.common.dto.CommonErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//
@RestControllerAdvice
public class CommonExceptionHandler {

    // Controller 단에서 발생하는 모든 예외를 일괄 처리하는 클래스
    // 실제 예외는 service계층에서 발생하지만, 따로 예외 처리가 없는 경우
    // 메소드를 호출한 상위 게층으로 전파됨.

    // 옳지 않은 입력값을 전달 시 호출되는 메소드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalHandler(IllegalArgumentException e) {
        e.printStackTrace();

        CommonErrorDto errorDTO = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);

    }

    // Entity를 DB에서 찾지 못했을 때 발생하는 예외를 처리하는 메소드
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundHandler(EntityNotFoundException e) {
        e.printStackTrace();

        CommonErrorDto errorDTO = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> authDeniedHandler(AccessDeniedException e) {
        e.printStackTrace();

        CommonErrorDto errorDTO = new CommonErrorDto(HttpStatus.FORBIDDEN, e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    // 미처 준비하지 못한 타입의 예외가 발생했을 시 처리할 메소드
    // 혹시 모르니 모든 예외를 처리하는 메소드를 준비하는 것이 권장됨.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        e.printStackTrace();

        CommonErrorDto errorDTO = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR
                , "서버 에러");

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR); // 500 에러

    }

}
