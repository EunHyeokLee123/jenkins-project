package com.springstudy.courseservice.client;

import com.springstudy.courseservice.common.dto.CommonResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;


// course 브랜치를 병합받았는데, 선인이 되어있지 않아서, 따로 만든 클라이언트 입니다!
@FeignClient(name = "eval-service")
public interface EvalClient {

    @PostMapping("eval/course-eval-rating-feign")
    CommonResDto<Map<Long, Double>> findCoursesRatingFeign(List<Long> productIds);


}
