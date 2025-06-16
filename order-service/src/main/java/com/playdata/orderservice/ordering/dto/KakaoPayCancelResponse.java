package com.playdata.orderservice.ordering.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayCancelResponse {

    private String aid;                 // 요청 고유 번호 (승인/취소 요청별 고유)
    private String tid;                 // 결제 고유 번호 (승인 시 발급된 번호)
    private String cid;                 // 가맹점 코드
    private String status;              // 결제 상태 (예: CANCEL_PAYMENT)

    // 취소 금액 관련
    private Amount amount;              // 취소된 금액 정보
    private String item_name;           // 상품명
    private String item_code;           // 상품 코드
    private int quantity;               // 수량
    private String created_at;          // 결제 준비 요청 시각
    private String approved_at;         // 결제 승인 시각
    private String canceled_at;         // 결제 취소 시각 (취소 응답에만 있는 주요 필드)
    private String payload;             // 결제 승인/취소 요청에 대해 저장한 값

    // Amount 클래스는 카카오페이 응답에서 금액 정보를 구조화하여 제공하는 경우가 많습니다.
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        private int total;              // 현재까지 취소된 총 금액
        private int tax_free;           // 현재까지 취소된 비과세 금액
        private int vat;                // 현재까지 취소된 부가세 금액
        private int point;              // 현재까지 취소된 포인트 금액
        private int discount;           // 현재까지 취소된 할인 금액
        private int green_deposit;
        // 추가: remaining_amount (잔액) 등 필요 시
        private RemainingAmount remaining_amount; // 취소 후 남은 금액 정보 (부분 취소 시 중요)
    }

    @Getter @Setter @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemainingAmount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;
        private int green_deposit;
    }
}