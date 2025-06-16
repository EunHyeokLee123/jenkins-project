package com.playdata.orderservice.ordering.dto;

import lombok.*;

import java.util.Date;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayDTO {
    private String tid; // 결제 고유 번호
    private String next_redirect_pc_url; // web - 받는 결제 페이지. 마지막 return값으로 들어가 결제가 완료되면 해당 주소로 이동하게
    private Date created_at;
}
