package com.ball.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;

@EnableRetry
@SpringBootApplication
public class BallComAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(BallComAuthApp.class, args);
    }

    @Bean
    public Hasher encoder() {
        return BCrypt.withDefaults();
    }

}
