package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.models.rest.UserName;
import com.ball.support.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class EventHandler {

    private final UserRepository repository;

    public EventHandler(UserRepository repository) {
        this.repository = repository;
    }

    public void handle(AuthEvent event) {
        String email = event.getEmail();
        String firstName = "Unknown";
        String lastName = "User";
        if (event.getMeta().containsKey("firstName")) firstName = event.getMeta().get("firstName");
        if (event.getMeta().containsKey("lastName")) lastName = event.getMeta().get("lastName");
        UserName userName = new UserName(email, firstName, lastName);
        this.repository.save(userName);
    }

}
