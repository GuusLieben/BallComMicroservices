package com.ball.auth;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;

@SpringBootApplication
public class BallComAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(BallComAuthApp.class, args);
    }

    @Value("${ball.rabbitmq.queue}")
    private String queueKey;

    @Bean
    public Queue getAuthQueue() {
        return new Queue(this.queueKey, false);
    }

    @Bean
    public Hasher encoder() {
        return BCrypt.withDefaults();
    }

}
