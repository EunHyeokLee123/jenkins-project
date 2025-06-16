package com.playdata.userservice.client;


import com.playdata.userservice.client.dto.CourseResDto;
import com.playdata.userservice.client.dto.OrderingListResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//@FeignClient(name = "course-service")
public interface OrderingServiceClient {

    @GetMapping("ordering/list")

    List<OrderingListResDto> getAllCourses();
}
