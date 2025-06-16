package com.springstudy.courseservice.repository;

import com.springstudy.courseservice.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  CourseRepository extends JpaRepository<Course, Long> {

    // 카테고리별 조회
    List<Course> findByCategory(String category);

    // 제목에 키워드를 포함하는 강의 검색
    List<Course> findByProductNameContaining(String keyword);

    // 페이징과 카테고리로 조회
    Page<Course> findByCategory(String category, Pageable pageable);

    //userId로 Course 얻기
    List<Course> findByUserId(Long userId);

}