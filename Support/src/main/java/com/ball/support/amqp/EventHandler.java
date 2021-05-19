package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.models.rest.UserName;
import com.ball.support.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class EventHandler {

    public void handle(AuthEvent event) {
    }

}
