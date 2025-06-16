package com.playdata.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

// feign 클라이언트를 사용하는 서비스에 추가
@EnableFeignClients
@EnableScheduling
public class PostserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostserviceApplication.class, args);
    }

}
