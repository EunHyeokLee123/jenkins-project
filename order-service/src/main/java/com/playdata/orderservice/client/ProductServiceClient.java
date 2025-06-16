package com.playdata.orderservice.client;

import com.playdata.orderservice.common.dto.CommonResDto;
import com.playdata.orderservice.ordering.dto.ProdDetailResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "course-service")
public interface ProductServiceClient {

    @GetMapping("/courses/info/{id}")
    ProdDetailResDto findById(@PathVariable Long id);

    @PostMapping("/courses/products")
    CommonResDto<List<ProdDetailResDto>> getProducts(@RequestBody List<Long> productIds);

    @PutMapping("/courses/cancel")
    ResponseEntity<?> cancelProduct(@RequestBody Map<Long, Integer> map);

    @PostMapping("/courses/findCourses")
    CommonResDto<List<ProdDetailResDto>> getProductsByUserId(@RequestBody Map<String, Long> userId);
}









