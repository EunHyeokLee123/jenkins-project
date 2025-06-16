package com.playdata.evalservice.client;

import com.playdata.evalservice.common.auth.TokenUserInfo;
import com.playdata.evalservice.common.dto.CommonResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name= "order-service")
public interface OrderServiceClient {

    @GetMapping("/order/ordereduser/{id}")
    CommonResDto<List<Long>> userBuyIt(@PathVariable(name = "id") Long userId);

}
