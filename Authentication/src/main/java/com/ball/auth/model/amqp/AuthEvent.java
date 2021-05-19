package com.ball.auth.model.amqp;

import com.ball.auth.model.User;
import com.ball.auth.model.UserRole;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

@Getter
public class AuthEvent implements Serializable {

    private final UUID guid;
    private final Map<String, String> meta;
    private final String email;
    private final UserRole role;

    public AuthEvent(User user) {
        this.guid = user.getGuid();
        this.meta = user.getMeta();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
