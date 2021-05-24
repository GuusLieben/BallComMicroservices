package com.ball.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class BallComSupport {

    public static void main(String[] args) {
        SpringApplication.run(BallComSupport.class, args);
    }

}
