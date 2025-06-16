package com.playdata.orderservice.ordering.controller;

import com.playdata.orderservice.common.auth.TokenUserInfo;
import com.playdata.orderservice.common.dto.CommonResDto;
import com.playdata.orderservice.common.dto.CreateOrderRequest;
import com.playdata.orderservice.ordering.dto.*;
import com.playdata.orderservice.ordering.dto.OrderingListResDto;
import com.playdata.orderservice.ordering.dto.OrderingSaveReqDto;
import com.playdata.orderservice.ordering.entity.Ordering;
import com.playdata.orderservice.ordering.service.OrderingService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderingController {

    private final OrderingService orderingService;


    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal TokenUserInfo userInfo,
                                         @RequestBody List<OrderingSaveReqDto> dtoList) {

        //유저정보, 주문요청데이터 받아옴
        System.out.println("createOrder 들어옴");
        log.info("/order/create: POST 요청, userInfo: {}", userInfo);
        log.info("dtoList: {}", dtoList);

        List<Ordering> orderings = orderingService.createOrder(userInfo, dtoList);

        CommonResDto resDto = new CommonResDto<>(HttpStatus.CREATED, "정상 주문 완료", orderings);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }


    // 주문 상태를 취소로 변경하는 요청
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable long id) {
        Ordering ordering = orderingService.cancelOrder(id);
        CommonResDto resDto
                = new CommonResDto(HttpStatus.OK, "주문 취소 완료", ordering.getId());
        return ResponseEntity.ok().body(resDto);
    }


    // 내 주문 내역 볼 수 있는 MyOrders
    @GetMapping("/my-order")
    public ResponseEntity<?> myOrder(
            @AuthenticationPrincipal TokenUserInfo userInfo) {
        List<OrderingListResDto> dtos = orderingService.myOrder(userInfo);
        CommonResDto<List<OrderingListResDto>> resDto =
                new CommonResDto<>(HttpStatus.OK, "정상 조회 완료", dtos);
        return ResponseEntity.ok(resDto);
    }

    // 내가 학습 가능한 강의 조회
    @GetMapping("/dashboard")
    public ResponseEntity<?> myDashboard(
            @AuthenticationPrincipal TokenUserInfo userInfo) {
        List<OrderingListResDto> dtos = orderingService.myDashboard(userInfo);
        CommonResDto<List<OrderingListResDto>> resDto =
                new CommonResDto<>(HttpStatus.OK, "정상 조회 완료", dtos);
        return ResponseEntity.ok(resDto);
    }


    // 강사용 본인 강의 주문 내역 조회
    @GetMapping("/my-course-order")
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal TokenUserInfo userInfo) {

//        if (!userInfo.getRole().equals(Role.ADMIN)) {
//            throw new AccessDeniedException("관리자만 접근 가능합니다.");
//        }
        List<OrderingListResDto> dtos = orderingService.myCourseOrder(userInfo);
        CommonResDto<List<OrderingListResDto>> resDto =
                new CommonResDto<>(HttpStatus.OK, "전체 주문 내역 조회 완료", dtos);
        return ResponseEntity.ok(resDto);
    }

    @PostMapping("/pay/ready")
    public KakaoPayDTO payReady(@AuthenticationPrincipal TokenUserInfo userInfo, @RequestBody List<OrderingSaveReqDto> dtoList) {
        // 카카오 결제 준비하기
        KakaoPayDTO readyResponse = orderingService.payReady(userInfo, dtoList);
        log.info("Calling SessionUtils.addAttribute to save tid: {}", readyResponse.getTid());
        log.info("결제 고유번호: " + readyResponse.getTid());

        return readyResponse;
    }

    @GetMapping("/pay/completed")
    public void payCompleted(@RequestParam("pg_token") String pgToken,
                             @RequestParam("orderId") String partnerOrderId, HttpServletResponse response) throws IOException {
        log.info("승인 단계");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("카카오페이 partner_order_id: " + partnerOrderId);

        // 카카오 결제 요청하기
        KakaoPayAproveResponse approveResponse = orderingService.payApprove(partnerOrderId, pgToken);
        log.info("결제 고유번호: " + approveResponse.getTid());

        // 팝업 닫기 HTML 응답
        String html = """
                <html><body>
                <script>
                  const payload = {
                    tid: '%s',
                    orderId: '%s'
                  };
                
                  if (window.opener) {
                    window.opener.postMessage({ type: 'KAKAO_PAY_SUCCESS', payload }, '*');
                  }
                
                  window.close();
                </script>
                <p>결제가 완료되었습니다. 창이 곧 닫힙니다...</p>
                </body></html>
                """.formatted(
                approveResponse.getTid(),
                approveResponse.getPartner_order_id()
        );
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(html);

    }

    //결제 진행 중 취소
    @GetMapping("/pay/cancel")
    public void cancel() {
        log.info("결제 취소");
    }

    //결제 실패
    @GetMapping("/pay/fail")
    public void fail() {
        log.info("결제 실패");
    }

    @PatchMapping("/refund")
    public ResponseEntity<?> refund(@RequestParam("orderId") Long id) {

        log.info("구매 환불");
        log.info("주문 번호 orderId: {}", id);

        KakaoPayCancelResponse resDto = orderingService.kakaoCancel(id);

        return ResponseEntity.ok().body(resDto);
    }

    // 강의 평가 등록 시, 수강한 유저만 등록이 가능하게끔 하기 위해 확인 하는 로직
    // 유사 로직이 있지만, feign 전용이라서 따로 선언함.
    @GetMapping("/ordereduser/{id}")
    public CommonResDto<?> getOrderedUser(@PathVariable(name = "id") Long userId) {
        List<Long> myOrdered = orderingService.findMyOrdered(userId);

        return new CommonResDto<>(HttpStatus.FOUND, "모든 강의 찾음", myOrdered);
    }

}
