package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.repository.UserEventRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthEventHandler implements Handler {

    private final UserEventRepository repository;

    public AuthEventHandler(UserEventRepository repository) {
        this.repository = repository;
    }

    public void handle(AuthEvent event) {
        event.setWritten(LocalDateTime.now());
        this.repository.save(event);
    }
}
