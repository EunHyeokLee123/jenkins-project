package com.playdata.evalservice.common.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

// 강사님과 만든 user-service와 통신을 위해 선언한 클래스
// 추후에 삭제할 예정임.


// 타 entity에서 사용 가능한 형태로 만드는 어노테이션
// 다양한 곳에서 사용할 entity라서 Embeddable 선언함.
@Embeddable
@Getter @NoArgsConstructor
@AllArgsConstructor
@Builder @ToString
public class Address {

    private String city;
    private String street;
    private String zipCode; // 우편번호

}
