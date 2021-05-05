package com.ball.auth;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BallComAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(BallComAuthApp.class, args);
    }

    @Value("${ball.rabbitmq.queue}")
    private String queueKey;

    @Bean
    public Queue getShipmentQueue() {
        return new Queue(this.queueKey, false);
    }

}
