package com.springstudy.courseservice.controller;

import com.springstudy.courseservice.dto.CourseResponseDto;
import com.springstudy.courseservice.service.CourseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeController {

    private final CourseService courseService;

    public HomeController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseResponseDto>> getAllCoursesFromRoot(Pageable pageable) {
        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }
}