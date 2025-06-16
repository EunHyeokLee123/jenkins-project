package com.springstudy.courseservice.entity;


import com.springstudy.courseservice.dto.CourseRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;


    @Column(length = 2000)
    private String description;

    private int price;

    @Column(name = "user_id")
    private Long userId;  // 강사 ID (User Service의 유저 ID)

    private String category;  // 카테고리 필드 추가

    private boolean active; // 강의 활성화 여부

    private String filePath;      // 저장된 파일 경로 (또는 S3 URL)

    // ✅ 도메인 메서드 (DDD 스타일 업데이트 메서드)
    public void updateCourse(String productName, String description, int price, String category, String filePath) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.filePath = filePath;
    }




    public void updateCourseInfo(CourseRequestDto dto) {
        this.productName = dto.getProductName();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.category = dto.getCategory();
        this.filePath = dto.getFilePath();
    }

    public void deactivate() {
        this.active = false;
    }

}