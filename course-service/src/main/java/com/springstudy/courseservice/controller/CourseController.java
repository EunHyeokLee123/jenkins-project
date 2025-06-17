package com.springstudy.courseservice.controller;

import com.springstudy.courseservice.common.auth.TokenUserInfo;
import com.springstudy.courseservice.common.dto.CommonResDto;
import com.springstudy.courseservice.dto.CourseRequestDto;
import com.springstudy.courseservice.dto.CourseResponseDto;
import com.springstudy.courseservice.entity.Course;
import com.springstudy.courseservice.repository.CourseRepository;
import com.springstudy.courseservice.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 강의 등록 (강사용)
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @RequestBody List<CourseRequestDto> dtoList) {

        List<Course> course = courseService.createCourse(userInfo, dtoList);

        CommonResDto resDto = new CommonResDto<>(HttpStatus.CREATED, "정상 등록 완료", course);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }


    // 강의 목록(기본 페이지)
    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses(Pageable pageable) {
        log.info("/product/list: GET, pageable: {}", pageable);
        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }

    // 정렬해서 조회
    @GetMapping("/all/sort")
    public ResponseEntity<Page<CourseResponseDto>> getSortedCourses(
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        return ResponseEntity.ok(courseService.getCoursesSorted(sort, page, size));
    }

    // 페이징 조회
    @GetMapping("/list")
    public ResponseEntity<Page<CourseResponseDto>> getCoursesByPage(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(courseService.getCoursesByPage(page, size));
    }

    // 카테고리별 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<CourseResponseDto>> getCoursesByCategory(@PathVariable String category) {
        if (category.equals("HTMLCSS")) {
            category = "HTML/CSS";
        }
        return ResponseEntity.ok(courseService.getCoursesByCategory(category));
    }


    // 카테고리 + 정렬 + 페이징
    @GetMapping("/category/{category}/sort")
    public ResponseEntity<Page<CourseResponseDto>> getCoursesByCategorySorted(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "") String sort) {

        if (category.equals("HTMLCSS")) {
            category = "HTML/CSS";
        }

        return ResponseEntity.ok(courseService.getCoursesByCategoryAndSort(category, page, size, sort));
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<List<CourseResponseDto>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }


    // 강의 상세
    @GetMapping("/info/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }


    // 강의 수정
    @PostMapping("/edit/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @RequestBody CourseRequestDto request,
                                          @AuthenticationPrincipal TokenUserInfo userInfo) {
        return ResponseEntity.ok(courseService.updateCourse(id, request, userInfo));
    }

    // 강의 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id,
                                          @AuthenticationPrincipal TokenUserInfo userInfo) {
        courseService.deleteCourse(id, userInfo);
        return ResponseEntity.noContent().build();
    }

    // 한 사용자의 모든 주문 내역 안에 있는 상품 정보를 리턴하는 메서드
    @PostMapping("/products")
    public ResponseEntity<?> getProducts(@RequestBody List<Long> productIds) {
        List<CourseResponseDto> productDtos = courseService.getCourseById(productIds);

        log.info(productDtos.toString());

        // CommonResDto로 감싸서 반환
        CommonResDto<List<CourseResponseDto>> resDto = new CommonResDto<>(HttpStatus.OK, "조회 완료", productDtos);

        log.info(resDto.toString());

        return ResponseEntity.ok(resDto);
    }


    // 강사의 본인 강의 정보 리턴하는 메서드
    @PostMapping("/findCourses")
    public ResponseEntity<?> getProductsByUserId(@RequestBody Map<String, Long> user) {
        Long userId = user.get("userId");
        System.out.println("userId = " + userId);
        List<Course> courseList = courseService.findByUserId(userId);

        // id만 뽑아서 List<Long>으로 변환
        List<Long> productIds = courseList.stream()
                .map(Course::getProductId)
                .collect(Collectors.toList());
        System.out.println("productIds = " + productIds);

        List<CourseResponseDto> productDtos = courseService.getCourseById(productIds);

        log.info(productDtos.toString());

        // CommonResDto로 감싸서 반환
        CommonResDto<List<CourseResponseDto>> resDto = new CommonResDto<>(HttpStatus.OK, "조회 완료", productDtos);

        log.info(resDto.toString());

        return ResponseEntity.ok(resDto);

    }


    // 댓글 작성 시 강사 userId 조회를 위한 메소드입니다.
    @GetMapping("/find/userid")
    public CommonResDto<?> getuserIdByCourseId(@RequestParam Long courseId) {
        CourseResponseDto foundCourse = courseService.getCourseById(courseId);

        CourseResponseDto build = CourseResponseDto.builder()
                .productId(foundCourse.getProductId())
                .productName(foundCourse.getProductName())
                .price(foundCourse.getPrice())
                .description(foundCourse.getDescription())
                .userId(foundCourse.getUserId())
                .category(foundCourse.getCategory())
                .active(foundCourse.isActive())
                .build();

        return new CommonResDto(HttpStatus.OK, "해당 강의 찾음", build);

    }



}