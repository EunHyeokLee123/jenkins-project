package com.playdata.userservice.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

   @CreationTimestamp
   private LocalDateTime createTime;

   @UpdateTimestamp
   private LocalDateTime updateTime;
}
