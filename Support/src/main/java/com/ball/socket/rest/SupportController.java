package com.ball.socket.rest;

import com.ball.socket.models.SocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class SupportController {

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("chat")
    public SocketMessage chat(@RequestBody SocketMessage message) {
        // TODO: Keep track of user registration events, obtain user from message sender if sender is a valid email
        // - If the sender is not a valid email, redact the message (source should ALWAYS be a valid user with a valid email)
        // - If the message is anonymous, use a random name (stored!)
        // - If the message is not anonymous, use the user's real name (instead of an email)
        // - Ensure random name is unique for topic + email (generate new if message type is JOIN)

        if (message.isAnonymous()) {
            message.setSender("Geheime Kabouter");
        }
        return message;
    }

}
