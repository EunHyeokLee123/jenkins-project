package com.playdata.evalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class EvalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvalServiceApplication.class, args);
    }

}
