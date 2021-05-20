package com.ball.support.amqp;

import com.ball.support.models.amqp.AuthEvent;
import com.ball.support.models.rest.UserName;
import com.ball.support.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserNameHandler implements Handler {

    private final UserRepository repository;

    public UserNameHandler(UserRepository repository) {
        this.repository = repository;
    }

    public void created(AuthEvent event) {
        String email = event.getEmail();
        String firstName = "Unknown";
        String lastName = "User";
        if (event.getMeta().containsKey("firstName")) firstName = event.getMeta().get("firstName");
        if (event.getMeta().containsKey("lastName")) lastName = event.getMeta().get("lastName");
        UserName userName = new UserName(email, firstName, lastName);
        this.repository.save(userName);
    }

    public void updated(AuthEvent event) {
        // Overwrites existing usernames with the latest information
        this.created(event);
    }

    public void deleted(AuthEvent event) {
        String email = event.getEmail();
        this.repository.deleteById(email);
    }

}
