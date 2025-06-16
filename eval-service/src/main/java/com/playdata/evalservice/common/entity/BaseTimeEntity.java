package com.playdata.evalservice.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
// 테이블과는 관련이 없고, 컬럼 정보만 자식에게 제공하기 위해 사용하는 어노테이션
@MappedSuperclass
// entity가 아니기에, 직접적인 객체 생성을 방지하기 위해 abstract 선언.
// 직접 사용되지 않고, 반드시 상속을 통해 구현되어야 한다는 것을 강조
public abstract class BaseTimeEntity {

    @CreationTimestamp // 객체를 생성한 시각
    private LocalDateTime createTime;

    @UpdateTimestamp // 객체 정보를 수정한 시각
    private LocalDateTime updateTime;


}
