package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.repository.UserEventRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthEventHandler implements Handler {

    private final UserEventRepository repository;

    public AuthEventHandler(UserEventRepository repository) {
        this.repository = repository;
    }

    public void handle(AuthEvent event) {
        this.repository.save(event);
    }
}
