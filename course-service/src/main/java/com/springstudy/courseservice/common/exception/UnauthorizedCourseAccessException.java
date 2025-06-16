package com.springstudy.courseservice.common.exception;

public class UnauthorizedCourseAccessException extends RuntimeException {
    public UnauthorizedCourseAccessException(String message) {
        super(message);
    }
}